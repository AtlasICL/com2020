import os
import requests
import threading
from typing import Any

import base64
import hashlib
import secrets

import webbrowser
from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import urlencode, urlparse, parse_qs


ISSUER = "https://accounts.google.com"
CLIENT_ID = os.environ.get("OIDC_CLIENT_ID")
CLIENT_SECRET = os.environ.get("OIDC_CLIENT_SECRET")

# From Google's API documentation: "The client ID and client secret
# obtained from the API Console are embedded in the source code of
# your application. In this context, the client secret is 
# obviously not treated as a secret." 
# The above quote is from "https://googleapis.dev/ruby/google-api-client/v0.36.3/file.oauth-installed.html".
# This is confirmed by "a client secret, which you embed in the source
#  code of your application. (In this context, the client secret is 
# obviously not treated as a secret.)". This quote is from "https://developers.google.com/identity/protocols/oauth2".

SCOPES = ["openid", "profile", "email"]


def validate_env_vars() -> None:
    """
    Helper function which verifies all necessary environment
    variables are set.
    """
    if not ISSUER:
        raise KeyError("OIDC_ISSUER environment variable is not set.")
    if not CLIENT_ID:
        raise KeyError("OIDC_CLIENT_ID environment variable is not set.")
    if not CLIENT_SECRET:
        raise KeyError("OIDC_SECRET environment variable is not set.")
    return


def get_oauth_config():
    url = str(ISSUER) + "/.well-known/openid-configuration"
    req = requests.get(url=url, timeout=10)
    req.raise_for_status()
    return req.json()
    

def open_browser(url: str) -> None:
    """
    Helper function to open the web browser.
    TODO: Compatible with different OSs?
    """
    webbrowser.open(url)


def b64url(data: bytes) -> str:
    """Returns base 64 encoded url."""
    return base64.urlsafe_b64encode(data).rstrip(b"=").decode("ascii")


def make_pkce_pair() -> tuple[str, str]:
    code_verifier = b64url(secrets.token_bytes(32))
    code_challenge = b64url(hashlib.sha256(code_verifier.encode("ascii")).digest())
    return code_verifier, code_challenge


def server_run() -> tuple[HTTPServer, threading.Thread]:
    """
    Starts the callback handler server in a separate thread.
    Returns the server instance and the created thread.
    """
    server = HTTPServer(("127.0.0.1", 0), CallbackHandler)
    thread = threading.Thread(
        target=server.handle_request,
        daemon=True
    )
    thread.start()
    return server, thread


class CallbackHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        parsed_url = urlparse(self.path)
        if parsed_url.path != "/callback":
            self.send_response(404)
            self.end_headers()
            return

        q = parse_qs(parsed_url.query)
        self.server.auth_code = q.get("code", [None])[0] # type: ignore
        self.server.auth_state = q.get("state", [None])[0] # type: ignore
        self.server.auth_error = q.get("error", [None])[0] # type: ignore
        self.server.auth_error_desc = q.get("error_description", [None])[0] # type: ignore

        self.send_response(200)
        self.send_header("Content-Type", "text/html; charset=utf-8")
        self.end_headers()
        self.wfile.write(
            b"<html><body><h3>Login complete. You can now close this tab.</h3></body></html>"+
            b"<style>body { font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background: #2c2f33 } h3 { color: #9b59ff; font-size: 36px; font-weight: bold; align-items: center; justify-content: center; }</style>"
        )


def google_login() -> tuple[Any, str]:
    """
    Prompts the user to log in via Google.
    Opens browser to Google accounts log in page.
    Returns the name and unique identifier of the user.

    :return: User unique identifier, user name.
    :rtype: tuple[Any, str]
    :raises HTTPError: If an HTTP error occurs. 
    """
    validate_env_vars()

    oauth_config = get_oauth_config()
    auth_endpoint = oauth_config["authorization_endpoint"]
    token_endpoint = oauth_config["token_endpoint"]
    userinfo_endpoint = oauth_config["userinfo_endpoint"]

    server, thread = server_run()
    redirect_uri = f"http://127.0.0.1:{server.server_port}/callback"

    state = secrets.token_urlsafe(24)
    nonce = secrets.token_urlsafe(24)
    code_verifier, code_challenge = make_pkce_pair()

    params = {
        "response_type": "code",
        "client_id": CLIENT_ID,
        "redirect_uri": redirect_uri,
        "scope": " ".join(SCOPES),
        "state": state,
        "nonce": nonce,
        "code_challenge": code_challenge,
        "code_challenge_method": "S256",
    }
    auth_url = f"{auth_endpoint}?{urlencode(params)}"

    open_browser(auth_url)

    thread.join() # Callback complete
    server.server_close()

    if not getattr(server, "auth_code", None):
        raise RuntimeError("No authorization code received.")

    if server.auth_state != state: # type: ignore
        raise RuntimeError("State mismatch (possible CSRF).")

    token_resp = requests.post(
        token_endpoint,
        data={
            "grant_type": "authorization_code",
            "code": server.auth_code, # type: ignore
            "redirect_uri": redirect_uri,
            "client_id": CLIENT_ID,
            "client_secret": CLIENT_SECRET,
            "code_verifier": code_verifier
        },
        timeout=15,
    )
    if not token_resp.ok:
        print("---- AUTH ERROR OCCURRED ----")
        print("|  Token status:", token_resp.status_code)
        print("|  Token body:", token_resp.text)
        token_resp.raise_for_status()

    tokens = token_resp.json()

    userinfo_resp = requests.get(
        userinfo_endpoint,
        headers={"Authorization": f"Bearer {tokens['access_token']}"},
        timeout=15,
    )
    userinfo_resp.raise_for_status()
    userinfo = userinfo_resp.json()

    return userinfo.get("sub"), userinfo.get("name")


# TESTING
def main():
    print(google_login())

if __name__ == "__main__":
    main()