from __future__ import annotations
import json
import sys
from pathlib import Path

OIDC_CONFIG_DIRECTORY = "config"
OIDC_CONFIG_FILENAME = "oidc.json"
REQUIRED_OIDC_KEYS: tuple[str, ...] = (
    "OIDC_ISSUER",
    "OIDC_CLIENT_ID",
    "OIDC_CLIENT_SECRET",
)
class OIDCConfigError(RuntimeError):
    """config file is missing/invalid"""

def _default_config_candidates() -> list[Path]:
    project_root = Path(__file__).resolve().parents[2]
    executable_dir = Path(sys.executable).resolve().parent
    working_directory = Path.cwd()
    candidates = [
        project_root / OIDC_CONFIG_DIRECTORY / OIDC_CONFIG_FILENAME,
        working_directory / OIDC_CONFIG_DIRECTORY / OIDC_CONFIG_FILENAME,
        working_directory.parent / OIDC_CONFIG_DIRECTORY / OIDC_CONFIG_FILENAME,
        executable_dir / OIDC_CONFIG_DIRECTORY / OIDC_CONFIG_FILENAME,
        executable_dir.parent / OIDC_CONFIG_DIRECTORY / OIDC_CONFIG_FILENAME,
    ]

    unique_candidates: list[Path] = []
    seen_candidates: set[Path] = set()
    for candidate in candidates:
        resolved_candidate = candidate.resolve()
        if resolved_candidate in seen_candidates:
            continue
        unique_candidates.append(resolved_candidate)
        seen_candidates.add(resolved_candidate)

    return unique_candidates

def resolve_oidc_config_path(config_path: str | Path | None = None) -> Path:
    """
    resolves the path to the shared oidc config file
    """
    if config_path is not None:
        candidate = Path(config_path).expanduser().resolve()
        if candidate.is_file():
            return candidate
        raise OIDCConfigError(
            f"Could not find OIDC config file at {candidate}."
        )

    for candidate in _default_config_candidates():
        if candidate.is_file():
            return candidate

    raise OIDCConfigError(
        "could not find shared OIDC config file"
        "Expected config/oidc.json in the project root or install directory"
    )

def load_oidc_config(config_path: str | Path | None = None) -> dict[str, str]:
    """
    Loads the shared OIDC config file and validates the required keys
    """
    oidc_path = resolve_oidc_config_path(config_path)

    try:
        with oidc_path.open("r", encoding="utf-8") as file:
            raw_config = json.load(file)
    except json.JSONDecodeError as error:
        raise OIDCConfigError(
            f"Could not parse OIDC config at {oidc_path} - invalid JSON."
        ) from error

    if not isinstance(raw_config, dict):
        raise OIDCConfigError(
            f"OIDC config at {oidc_path} must contain a JSON object."
        )

    config: dict[str, str] = {}
    for key in REQUIRED_OIDC_KEYS:
        value = raw_config.get(key)
        if not isinstance(value, str) or not value.strip():
            raise OIDCConfigError(
                f"{key} is missing from OIDC config at {oidc_path}."
            )
        config[key] = value.strip()

    config["OIDC_ISSUER"] = config["OIDC_ISSUER"].rstrip("/")
    return config
