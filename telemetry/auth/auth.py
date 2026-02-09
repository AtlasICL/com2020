import os
import requests
import json

ISSUER = ""
CLIENT_ID = ""
CLIENT_SECRET = ""

def get_env_vars() -> None:
    global ISSUER, CLIENT_ID, CLIENT_SECRET
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


def get_oauth_config():
    url = str(ISSUER) + '/.well-known/openid-configuration'
    req = requests.get(url=url, timeout=10)
    return req.json()
    

def google_login():
    validate_env_vars()
    oauth_config = get_oauth_config()
    auth_endpoint = oauth_config["authorization_endpoint"]
    token_endpoint = oauth_config["token_endpoint"]
    



def main():
    google_login()


if __name__ == "__main__":
    main()