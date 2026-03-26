"""
Docstring for telemetry.auth.auth_wrapper

This module is a wrapper around the core authentication logic found at
telemetry.auth.auth

The wrapper is called directly (entry point at main()), and prints the
results of the login call into stdout in json form.
"""

import argparse
import json

from auth.auth import google_login


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--oidc-issuer",
        help="OIDC issuer URL."
    )
    parser.add_argument(
        "--oidc-client-id",
        help="OIDC client ID."
    )
    parser.add_argument(
        "--oidc-client-secret",
        help="OIDC client secret."
    )
    args = parser.parse_args()

    login_kwargs = {}
    if args.oidc_issuer is not None:
        login_kwargs["issuer"] = args.oidc_issuer
    if args.oidc_client_id is not None:
        login_kwargs["client_id"] = args.oidc_client_id
    if args.oidc_client_secret is not None:
        login_kwargs["client_secret"] = args.oidc_client_secret

    sub, name, role = google_login(**login_kwargs)
    output = {
        "name": name,
        "sub": sub,
        "role": role.value
    }
    print(json.dumps(output))


if __name__ == "__main__":
    main()
