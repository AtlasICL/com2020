"""
Docstring for telemetry.core.parsing

This module contains the logic for parsing game output json files. This
module is responsible for reading the event log JSON files, and
instantiating the ValidEvent object which corresponds to the event 
parsed.
"""

import json
from typing import TypeAlias
from datetime import datetime
from pathlib import Path

from core.events import (
    KillEnemy,
    EventParameter,
    BossEncounterFail,
    GainCoin,
    EventType,
    StartSession,
    BuyUpgrade,
    NormalEncounterStart,
    EndSession,
    NormalEncounterComplete,
    SettingsChange,
    BossEncounterComplete,
    NormalEncounterFail,
    BossEncounterStart,
)



ValidEvent: TypeAlias = (
    StartSession
    | NormalEncounterStart
    | NormalEncounterComplete
    | NormalEncounterFail
    | BossEncounterStart
    | BossEncounterComplete
    | BossEncounterFail
    | GainCoin
    | BuyUpgrade
    | EndSession
    | SettingsChange
    | KillEnemy
)

    
def convert_time(time_string: str) -> datetime:
    """
    :param time_string: Custom formatted string of timestamp.
    :type time_string: str
    :return: Datetime version of time_string.
    :rtype: datetime
    """
    return datetime.strptime(time_string, "%Y/%m/%d/%H/%M/%S")


def get_file(filename: Path) -> list[dict]:
    """
    This function parses a json file and returns a list of json objects.
    
    :param filename: filename for json to be parsed.
    :type filename: Path
    :return: List of json objects.
    :rtype: list[dict[str, Any]]
    """
    try: 
        with open(filename, 'r') as f:
            return json.load(f)
    except FileNotFoundError:
        raise FileNotFoundError(f"Could not find file: {filename}")
    except json.JSONDecodeError:
        raise RuntimeError(f"Could not parse - invalid json.")


def parse_file(filename: Path) -> list[ValidEvent]:
    """
    Creates a list of ValidEvent objects from a json file.
    
    :param filename: filename for json to be parsed.
    :type filename: Path
    :return: List of valid event objects.
    :rtype: list[ValidEvent]
    """
    events = get_file(filename)
    event_objects = []
    for event in events:
        event_objects.append(parse_event(event))
    return event_objects


def parse_event(event: dict) -> ValidEvent:
    """
    Creates a ValidEvent object from a json object.
    
    :param event: json object for a single event.
    :type event: dict
    :return: Valid event object translated from the json object.
    :rtype: ValidEvent
    :raises RuntimeError: If an event of an unknown type is found, or 
    a required field is missing.
    """
    try:
        event_type = event[EventParameter.EVENT_TYPE]
    except KeyError:
        raise RuntimeError("Event must have an \"event\" field")
    
    try:
        match event_type:
            case EventType.START_SESSION:
                return StartSession(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.DIFFICULTY]
                )
            case EventType.END_SESSION:
                return EndSession(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP])
                )
            case EventType.NORMAL_ENCOUNTER_START:
                return NormalEncounterStart(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER]
                )
            case EventType.NORMAL_ENCOUNTER_COMPLETE:
                return NormalEncounterComplete(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER],
                    event[EventParameter.PLAYER_HP_REMAINING]
                )
            case EventType.NORMAL_ENCOUNTER_FAIL:
                return NormalEncounterFail(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER],
                    event[EventParameter.LIVES_LEFT]
                )
            case EventType.BOSS_ENCOUNTER_START:
                return BossEncounterStart(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER]
                )
            case EventType.BOSS_ENCOUNTER_COMPLETE:
                return BossEncounterComplete(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER],
                    event[EventParameter.PLAYER_HP_REMAINING]
                )
            case EventType.BOSS_ENCOUNTER_FAIL:
                return BossEncounterFail(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER],
                    event[EventParameter.LIVES_LEFT]
                )
            case EventType.GAIN_COIN:
                return GainCoin(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER],
                    event[EventParameter.COINS_GAINED]
                )
            case EventType.BUY_UPGRADE:
                return BuyUpgrade(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.STAGE_NUMBER],
                    event[EventParameter.COINS_SPENT],
                    event[EventParameter.UPGRADE_BOUGHT]
                )
            case EventType.SETTINGS_CHANGE:
                return SettingsChange(
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.SETTING],
                    event[EventParameter.SETTING_VALUE]
                )
            case EventType.KILL_ENEMY:
                return KillEnemy(
                    event[EventParameter.USER_ID],
                    event[EventParameter.SESSION_ID],
                    convert_time(event[EventParameter.TIMESTAMP]),
                    event[EventParameter.ENCOUNTER],
                    event[EventParameter.DIFFICULTY],
                    event[EventParameter.STAGE_NUMBER],
                    event[EventParameter.ENEMY_TYPE]
                )
            case _:
                raise RuntimeError(
                    f"Unexpected event type: {event_type}"
                )
    except KeyError as e:
        raise RuntimeError(
            f"An event of type {event_type} is missing the field {e}"
        )
