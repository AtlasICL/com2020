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
