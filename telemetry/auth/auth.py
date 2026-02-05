import os
import json
from logto import LogtoClient, LogtoConfig

ISSUER = os.environ.get("OIDC_ISSUER")
CLIENT_ID = os.environ.get("OIDC_CLIENT_ID")
CLIENT_SECRET = os.environ.get("OIDC_CLIENT_SECRET") 

def validate_env_vars() -> None:
    if not ISSUER:
        raise KeyError("OIDC_ISSUER environment variable is not set.")
    if not CLIENT_ID:
        raise KeyError("OIDC_CLIENT_ID environment variable is not set.")
    if not CLIENT_SECRET:
        raise KeyError("OIDC_CLIENT_SECRET environment variable is not set.")
    return

def google_login():
    validate_env_vars()
    client = LogtoClient(
        LogtoConfig(
            endpoint=ISSUER,
            appId=CLIENT_ID,
            appSecret=CLIENT_SECRET
        )
    )

def main():
    google_login()

if __name__ == "__main__":
    main()