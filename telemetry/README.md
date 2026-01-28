# Setup
The telemetry module uses python virtual environments. See OS-specific
instructions below.

**Windows**
```Powershell
python -m venv venv
.\venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
```

**Linux**
```sh
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

**MacOS**
```sh
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

To deactivate on any OS, use `deactivate`.

# Running
To run the telemetry app, make sure you are in the telemetry directory.
Then:
```
python ./telemetry_app.py
```