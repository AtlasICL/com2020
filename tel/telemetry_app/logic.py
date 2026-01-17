import json
from typing import TypeAlias

from events import EventType, EncounterName, UpgradeName, EnemyType, SettingName

class SessionStart:
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp

class NormalEncounterStart:
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

class NormalEncounterComplete:
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            player_HP_remaining: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.player_HP_remaining = player_HP_remaining

class NormalEncounterFail:
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

class NormalEncounterRetry:
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            lives_left: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.lives_left = lives_left

class BossEncounterStart:
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

class BossEncounterComplete:
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            player_HP_remaining: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.player_HP_remaining = player_HP_remaining
        
class BossEncounterFail:
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

class BossEncounterRetry:
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            lives_left: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.lives_left = lives_left

class GainCoin:
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            coins_gained: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.coins_gained = coins_gained

class BuyUpgrade:
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            coins_spent: int,
            upgrade_bought: UpgradeName
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.coins_spent = coins_spent
        self.upgrade_bought = upgrade_bought

class EndSession:
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp

class SettingsChange:
    def __init__(
        self,
        userID: int,
        sessionID: int,
        timestamp: str,
        encounter_name: EncounterName,
        setting: SettingName,
        value: int
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.setting = setting
        self.value = value

class KillEnemy:
    def __init__(
        self,
        userID: int,
        sessionID: int,
        timestamp: str,
        encounter_name: EncounterName,
        stage_number: int,
        enemy_type: EnemyType
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.enemy_type = enemy_type

class StartTelemetry:
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str
    ):
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp


ValidEvent: TypeAlias = SessionStart | NormalEncounterStart | NormalEncounterComplete | NormalEncounterFail | NormalEncounterRetry | BossEncounterStart | BossEncounterComplete | BossEncounterFail | BossEncounterRetry | GainCoin | BuyUpgrade | EndSession | SettingsChange | KillEnemy | StartTelemetry 

def parse_file(filename: str):
    try: 
        with open(filename, 'r') as f:
            return json.load(f)
    except FileNotFoundError:
        raise FileNotFoundError(f"Could not find file: {filename}")
    except json.JSONDecodeError:
        raise RuntimeError(f"Could not parse - invalid json.")

def parse_event(event: dict) -> ValidEvent:
    return StartTelemetryEvent()
    
def main():
    events = parse_file("example_data.json")
    print(type(events))
    print(events)

if __name__ == "__main__":
    main()
