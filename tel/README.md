# Telemetry app instructions

## Setup
The telemetry module uses python virtual environments.  
See OS-specific instructions below.

### Windows
1) Create a local venv
   ```
   python -m venv venv
   ```
2) Activate it
   ```Powershell
   ./venv/Scripts/Activate.ps1
   ```
    If you haven't yet set your execution policy, Windows might ask you to do that
at this stage.
3) Install dependencies
   ```
   pip install -r requirements.txt
   ```

### Linux
1) Create a local venv
   ```
   python3 -m venv venv
   ```
2) Activate it
   ```sh
   source venv/bin/activate
   ```
3) Install dependencies
   ```
   pip install -r requirements.txt
   ```

### MacOS
1) Create a local venv
   ```
   python3 -m venv venv
   ```
2) Activate it
   ```sh
   source venv/bin/activate
   ```
3) Install dependencies
   ```
   pip install -r requirements.txt
   ```

To deactivate on any OS, use `deactivate`.