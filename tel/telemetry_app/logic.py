import json

from tel.telemetry_app.events import EventType, EncounterName, UpgradeName, EnemyType, SettingName

def parse_file(filename: str):
    try: 
        with open(filename, 'r') as f:
            return json.load(f)
    except FileNotFoundError:
        raise FileNotFoundError(f"Could not find file: {filename}")
    except json.JSONDecodeError:
        raise RuntimeError(f"Could not parse - invalid json.")
    

