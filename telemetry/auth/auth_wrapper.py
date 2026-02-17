"""
Docstring for telemetry.auth.auth_wrapper

This module is a wrapper around the core authentication logic found at
telemetry.auth.auth

The wrapper is called directly (entry point at main()), and prints the
results of the login call into stdout in json form.

Please note that, much like the core auth module, this module requires
the relevant environment variables to be set, namely OIDC_ISSUER, 
OIDC_CLIENT_ID, and OIDC_CLIENT_SECRET.
"""

import json

from auth.auth import google_login

def main():
    sub, name, role = google_login()
    output = {
        "name": name,
        "sub": sub,
        "role": role.value
    }
    print(json.dumps(output))
    return

if __name__ == "__main__":
    main()
