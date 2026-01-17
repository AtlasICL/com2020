import json
from typing import TypeAlias

from events import EventType, EncounterName, EventParameter, UpgradeName, EnemyType, SettingName

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

def get_file(filename: str) -> list[dict]:
    try: 
        with open(filename, 'r') as f:
            return json.load(f)
    except FileNotFoundError:
        raise FileNotFoundError(f"Could not find file: {filename}")
    except json.JSONDecodeError:
        raise RuntimeError(f"Could not parse - invalid json.")

def parse_file(filename: str) -> list[ValidEvent]:
    events = get_file(filename)
    event_objects = []
    for event in events:
        event_objects.append(parse_event(event))
    return event_objects

def parse_event(event: dict) -> ValidEvent:
    match event["event"]:
        case EventType.START_TELEMETRY:
            return StartTelemetry(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP]
            )
        case EventType.SESSION_START:
            return SessionStart(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP]
            )
        case EventType.END_SESSION:
            return EndSession(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP]
            )
        case EventType.NORMAL_ENCOUNTER_START:
            return NormalEncounterStart(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER]
            )
        case EventType.NORMAL_ENCOUNTER_COMPLETE:
            return NormalEncounterComplete(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER],
                event[EventParameter.PLAYER_HP_REMAINING]
            )
        case EventType.NORMAL_ENCOUNTER_FAIL:
            return NormalEncounterFail(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER]
            )
        case EventType.NORMAL_ENCOUNTER_RETRY:
            return NormalEncounterRetry(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER],
                event[EventParameter.LIVES_LEFT]
            )
        case EventType.BOSS_ENCOUNTER_START:
            return BossEncounterStart(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER]
            )
        case EventType.BOSS_ENCOUNTER_COMPLETE:
            return BossEncounterComplete(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER],
                event[EventParameter.PLAYER_HP_REMAINING]
            )
        case EventType.BOSS_ENCOUNTER_FAIL:
            return BossEncounterFail(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER]
            )
        case EventType.BOSS_ENCOUNTER_RETRY:
            return BossEncounterRetry(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER],
                event[EventParameter.LIVES_LEFT]
            )
        case EventType.GAIN_COIN:
            return GainCoin(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER],
                event[EventParameter.COINS_GAINED]
            )
        case EventType.BUY_UPGRADE:
            return BuyUpgrade(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER],
                event[EventParameter.COINS_SPENT],
                event[EventParameter.UPGRADE_BOUGHT]
            )
        case EventType.SETTINGS_CHANGE:
            return SettingsChange(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.SETTING],
                event[EventParameter.SETTING_VALUE]
            )
        case EventType.KILL_ENEMY:
            return KillEnemy(
                event[EventParameter.USER_ID],
                event[EventParameter.SESSION_ID],
                event[EventParameter.TIMESTAMP],
                event[EventParameter.ENCOUNTER],
                event[EventParameter.STAGE_NUMBER],
                event[EventParameter.ENEMY_TYPE]
            )
        case _:
            raise RuntimeError("Error parsing json.")
    
def main():
    parse_file("example_data.json")

if __name__ == "__main__":
    main()
