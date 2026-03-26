"""
Docstring for telemetry.auth.auth

This module is responsible for Google OAuth OIDC authentication.
This allows users of this module to get the name, unique stable 
identifier, and role of the authenticating user.
"""

import requests
import threading

import base64
import hashlib
import secrets

import json
from enum import Enum

import webbrowser
from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import urlencode, urlparse, parse_qs

# From Google's API documentation: "The client ID and client secret
# obtained from the API Console are embedded in the source code of
# your application. In this context, the client secret is 
# obviously not treated as a secret." 
# The above quote is from "https://googleapis.dev/ruby/google-api-client/v0.36.3/file.oauth-installed.html".
# This is confirmed by "a client secret, which you embed in the source
#  code of your application. (In this context, the client secret is 
# obviously not treated as a secret.)". This quote is from "https://developers.google.com/identity/protocols/oauth2".

SCOPES: list[str] = ["profile", "email"]
OIDC_ISSUER: str = "https://accounts.google.com"
OIDC_CLIENT_ID: str = "ADD_OIDC_CLIENT_ID"
OIDC_CLIENT_SECRET: str = "ADD_OIDC_CLIENT_ID"

def get_oauth_config(issuer: str) -> dict:
    url = issuer + "/.well-known/openid-configuration"
    req = requests.get(url=url, timeout=10)
    req.raise_for_status()
    return req.json()
    

def open_browser(url: str) -> None:
    """
    Helper function to open the web browser.
    """
    webbrowser.open(url)


def b64url(data: bytes) -> str:
    """
    Returns base 64 encoded url.
    """
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
    def do_GET(self) -> None:
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
            b"<html><body><h3>Login complete. " +
            b"You can now close this tab.</h3></body></html>" +
            b"""<style>body {
                font-family: Arial, sans-serif; 
                display: flex; 
                justify-content: center; 
                align-items: center; 
                height: 100vh; 
                background: #2c2f33 
            } 
            h3 {
                color: #9b59ff; 
                font-size: 36px; 
                font-weight: bold; 
                align-items: center; 
                justify-content: center;
            }</style>"""
        )
        

class Role(str, Enum):
    PLAYER = "player"
    DESIGNER = "designer"
    DEVELOPER = "developer"


def google_login(
    issuer: str = OIDC_ISSUER,
    client_id: str = OIDC_CLIENT_ID,
    client_secret: str = OIDC_CLIENT_SECRET,
) -> tuple[str, str, Role]:
    """
    Prompts the user to log in via Google.
    Opens browser to Google accounts log in page.
    Returns the name and unique identifier of the user.

    :return: User unique identifier, user name.
    :rtype: tuple[str, str, Role]
    :raises HTTPError: If an HTTP error occurs.
    """
    issuer = issuer.rstrip("/")
    oauth_config = get_oauth_config(issuer)
    auth_endpoint = oauth_config["authorization_endpoint"]
    token_endpoint = oauth_config["token_endpoint"]
    userinfo_endpoint = oauth_config["userinfo_endpoint"]

    server, thread = server_run()
    redirect_uri = f"http://127.0.0.1:{server.server_port}/callback"

    state = secrets.token_urlsafe(24)
    code_verifier, code_challenge = make_pkce_pair()

    params = {
        "response_type": "code",
        "client_id": client_id,
        "redirect_uri": redirect_uri,
        "scope": " ".join(SCOPES),
        "state": state,
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
        raise RuntimeError("AUTH ERROR - State mismatch.")

    token_resp = requests.post(
        token_endpoint,
        data={
            "grant_type": "authorization_code",
            "code": server.auth_code, # type: ignore
            "redirect_uri": redirect_uri,
            "client_id": client_id,
            "client_secret": client_secret,
            "code_verifier": code_verifier
        },
        timeout=25,
    )
    if not token_resp.ok:
        token_resp.raise_for_status()

    tokens = token_resp.json()

    userinfo_resp = requests.get(
        userinfo_endpoint,
        headers={"Authorization": f"Bearer {tokens['access_token']}"},
        timeout=15,
    )
    userinfo_resp.raise_for_status()
    userinfo = userinfo_resp.json()
    sub = userinfo.get("sub")
    return sub, userinfo.get("name"), get_role("logins_file.json", user_id=sub)


def get_role(filename: str, user_id: str) -> Role:
    """
    This function returns the role of the given user. It does so by
    checking the user roles json file at the provided filepath.
    If a profile does not exist for the given user, this function will
    create one with the default role of "player".

    :param filename: File path for the user roles json file.
    :type filename: str
    :param user_id: The user ID of the user.
    :type user_id: int
    :return: Returns the role of the user.
    :rtype: Role
    """
    try:
        with open(filename, 'r') as f:
            player_roles = json.load(f)
            this_user = player_roles.get(str(user_id))
            if this_user is None:
                player_roles[str(user_id)] = Role.PLAYER.value
                with open(filename, 'w') as outfile:
                    json.dump(player_roles, outfile, indent=4)
                return Role.PLAYER
            else:
                try:
                    return Role(this_user) 
                except ValueError:
                    raise ValueError(f"Logins file at {filename} " +
                                     f"contained an unknown role")
    except FileNotFoundError:
        raise FileNotFoundError(f"Could not find user roles file at {filename}")
    except json.JSONDecodeError:
        raise RuntimeError(
            f"Could not parse user roles file at {filename} " + 
            f"- invalid json."
        )
