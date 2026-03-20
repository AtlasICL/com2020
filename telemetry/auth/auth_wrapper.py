"""
Docstring for telemetry.auth.auth_wrapper

This module is a wrapper around the core authentication logic found at
telemetry.auth.auth

The wrapper is called directly (entry point at main()), and prints the
results of the login call into stdout in json form.

Please note that, much like the core auth module, this module requires
the shared OIDC config file at config/oidc.json.
"""

import argparse
import json

from auth.auth import google_login


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--oidc-config",
        help="Path to the shared OIDC config file."
    )
    args = parser.parse_args()

    sub, name, role = google_login(config_path=args.oidc_config)
    output = {
        "name": name,
        "sub": sub,
        "role": role.value
    }
    print(json.dumps(output))


if __name__ == "__main__":
    main()
