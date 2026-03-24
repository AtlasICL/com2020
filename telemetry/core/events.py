"""
Docstring for telemetry.core.events

This module defines the Event objects which are created upon parsing
JSON, and relevant Enums.
"""

from enum import Enum
from datetime import datetime
from dataclasses import dataclass


class EventType(str, Enum):
    """
    Enumerates the possible event types. These represent the telemetry
    events which are emitted by the game, and interpreted by the
    telemetry application.
    """
    START_SESSION = "StartSession"
    END_SESSION = "EndSession"

    NORMAL_ENCOUNTER_START = "NormalEncounterStart"
    NORMAL_ENCOUNTER_COMPLETE = "NormalEncounterComplete"
    NORMAL_ENCOUNTER_FAIL = "NormalEncounterFail"

    BOSS_ENCOUNTER_START = "BossEncounterStart"
    BOSS_ENCOUNTER_COMPLETE = "BossEncounterComplete"
    BOSS_ENCOUNTER_FAIL = "BossEncounterFail"

    GAIN_COIN = "GainCoin"
    BUY_UPGRADE = "BuyUpgrade"
    SETTINGS_CHANGE = "SettingsChange"
    KILL_ENEMY = "KillEnemy"


class EventParameter():
    """
    Helper class which stores the string literals corresponding to the
    various parameters passed in JSON format.
    """
    EVENT_TYPE = "event"
    USER_ID = "userID"
    SESSION_ID = "sessionID"
    TIMESTAMP = "timestamp"
    ENCOUNTER = "encounter_name"
    DIFFICULTY = "difficulty"
    STAGE_NUMBER = "stage_number"
    LIVES_LEFT = "lives_left"
    COINS_GAINED = "coins_gained"
    UPGRADE_BOUGHT = "upgrade_bought"
    COINS_SPENT = "coins_spent"
    PLAYER_HP_REMAINING = "player_HP_remaining"
    SETTING = "setting"
    SETTING_VALUE = "settingValue"
    JUSTIFICATION = "settingsChangeJustification"
    ENEMY_TYPE = "enemy_type"


class EncounterName(str, Enum):
    """
    Enumerates the possible encounters.
    """
    GOBLIN_ENCOUNTER = "GOBLIN_ENCOUNTER"
    FISHMAN_ENCOUNTER = "FISHMAN_ENCOUNTER"
    PYROMANCER_ENCOUNTER = "PYROMANCER_ENCOUNTER"
    EVIL_WIZARD_ENCOUNTER = "EVIL_WIZARD_ENCOUNTER"
    GOBLIN_DUO_ENCOUNTER = "GOBLIN_DUO_ENCOUNTER"
    GOBLIN_FISHMAN_ENCOUNTER = "GOBLIN_FISHMAN_ENCOUNTER"
    ARMOURED_GOBLIN_ENCOUNTER = "ARMOURED_GOBLIN_ENCOUNTER"
    PYROMANCER_FISHMAN_ENCOUNTER = "PYROMANCER_FISHMAN_ENCOUNTER"
    GHOST_ENCOUNTER = "GHOST_ENCOUNTER"
    ARMOURED_GOBLIN_PYROMANCER_ENCOUNTER = "ARMOURED_GOBLIN_PYROMANCER_ENCOUNTER"
    GOBLIN_FISHMAN_PYROMANCER_ENCOUNTER = "GOBLIN_FISHMAN_PYROMANCER_ENCOUNTER"
    BLACK_KNIGHT_ENCOUNTER = "BLACK_KNIGHT_ENCOUNTER"
    DRAGON_ENCOUNTER = "DRAGON_ENCOUNTER"


class UpgradeName(str, Enum):
    """
    Enumerates the possible upgrades players may buy.
    """
    PHYSICAL_DAMAGE_RESISTANCE = "PHYSICAL_DAMAGE_RESISTANCE"
    FIRE_DAMAGE_RESISTANCE = "FIRE_DAMAGE_RESISTANCE"
    WATER_DAMAGE_RESISTANCE = "WATER_DAMAGE_RESISTANCE"
    THUNDER_DAMAGE_RESISTANCE = "THUNDER_DAMAGE_RESISTANCE"
    IMPROVED_PHYSICAL_DAMAGE = "IMPROVED_PHYSICAL_DAMAGE"
    IMPROVED_FIRE_DAMAGE = "IMPROVED_FIRE_DAMAGE"
    IMPROVED_WATER_DAMAGE = "IMPROVED_WATER_DAMAGE"
    IMPROVED_THUNDER_DAMAGE = "IMPROVED_THUNDER_DAMAGE"
    SLASH_UNLOCK = "SLASH_UNLOCK"
    ABSOLUTE_PULSE_UNLOCK = "ABSOLUTE_PULSE_UNLOCK"
    WATER_JET_UNLOCK = "WATER_JET_UNLOCK"
    FIRE_BALL_UNLOCK = "FIRE_BALL_UNLOCK"
    THUNDER_STORM_UNLOCK = "THUNDER_STORM_UNLOCK"


class EnemyType(str, Enum):
    """
    Enumerates the types of enemies which may be present across
    encounters.
    """
    GOBLIN = "Goblin"
    FISHMAN = "Fishman"
    PYROMANCER = "Pyromancer"
    EVIL_WIZARD = "EvilWizard"
    ARMOURED_GOBLIN = "ArmouredGoblin"
    GHOST = "Ghost"
    BLACK_KNIGHT = "BlackKnight"
    DRAGON = "Dragon"


class SettingName(str, Enum):
    """
    Enumerates the possible settings which developers and designers may
    change.
    """
    TELEMETRY_ENABLED = "TELEMETRY_ENABLED"
    PLAYER_MAX_HEALTH = "PLAYER_MAX_HEALTH"
    ENEMY_DAMAGE_MULTIPLIER = "ENEMY_DAMAGE_MULTIPLIER"
    ENEMY_MAX_HEALTH_MULTIPLIER = "ENEMY_MAX_HEALTH_MULTIPLIER"
    STARTING_LIVES = "STARTING_LIVES"
    MAX_MAGIC = "MAX_MAGIC"
    MAGIC_REGEN_RATE = "MAGIC_REGEN_RATE"
    SHOP_ITEM_COUNT = "SHOP_ITEM_COUNT"
    ENCOUNTER_PAYOUT = "ENCOUNTER_PAYOUT"


class Difficulty(str, Enum):
    """
    Enumerates the possible difficulties the game may be set to.
    """
    EASY = "Easy"
    MEDIUM = "Medium"
    HARD = "Hard"

class Speed(str, Enum):
    """
    Enumerates the possible speeds of player progress.
    """
    FAST = "Fast"
    SLOW = "Slow"

class CoinHold(str, Enum):
    """
    Enumerates the possible length of time players hold onto coins.
    """
    LONG = "Long"
    SHORT = "Short"

@dataclass(frozen=True)
class StartSession:
    """
    StartSession object represents an instance of a 
    StartSession event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param difficulty: Difficulty of the session.
    :type difficulty: Difficulty
    """
    userID: str
    sessionID: int
    timestamp: datetime
    difficulty: Difficulty


@dataclass(frozen=True)
class NormalEncounterComplete:
    """
    NormalEncounterComplete object represents an instance of a
    NormalEncounterComplete event.
    
    :param userID: Unique ID of the user.
    :type userID: int
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player 
    has completed.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage player has completed.
    :type stage_number: int
    :param player_HP_remaining: Amount of health the player
    has remaining.
    :type player_HP_remaining: int
    """
    userID: int
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int
    player_HP_remaining: int


@dataclass(frozen=True)
class NormalEncounterStart:
    """
    NormalEncounterStart object represents an instance of a
    NormalEncounterStart event.

    :param userID: Unique ID of the user.
    :type userID: int
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player is starting.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage player is starting.
    :type stage_number: int
    """
    userID: int
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int

    
@dataclass(frozen=True)
class NormalEncounterFail:
    """
    NormalEncounterFail object represents an instance of a
    NormalEncounterFail event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player 
    has failed.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage player has failed.
    :type stage_number: int
    :param lives_left: Lives left at the end of an encounter.
    :type lives_left: int
    """
    userID: str
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int
    lives_left: int

    
@dataclass(frozen=True)
class BossEncounterStart:
    """
    BossEncounterStart object represents an instance of a
    BossEncounterStart event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player is starting.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage player is starting.
    :type stage_number: int
    """
    userID: str
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int


@dataclass(frozen=True)
class BossEncounterComplete:
    """
    BossEncounterComplete object represents an instance of a
    BossEncounterComplete event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player 
    has completed.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage player has completed.
    :type stage_number: int
    :param player_HP_remaining: Player health remaining once 
    boss encounter is complete.
    :type player_HP_remaining: int
    """
    userID: str
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int
    player_HP_remaining: int

    
@dataclass(frozen=True)
class BossEncounterFail:
    """
    BossEncounterFail object represents an instance of a
    BossEncounterFail event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player has failed.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage player has failed.
    :type stage_number: int
    :param lives_left: Lives left at the end of an encounter.
    :type lives_left: int
    """
    userID: str
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int
    lives_left: int


@dataclass(frozen=True)
class GainCoin:
    """
    GainCoin object represents an instance of a GainCoin event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player is in.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage number.
    :type stage_number: int
    :param coins_gained: Number of coins gained.
    :type coins_gained: int
    """
    userID: str
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int
    coins_gained: int


@dataclass(frozen=True)
class BuyUpgrade:
    """
    BuyUpgrade object represents an instance of an BuyUpgrade event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param stage_number: Current stage player is complete.
    :type stage_number: int
    :param coins_spent: Number of coins spent on 
    buying this upgrade.
    :type coins_spent: int
    :param upgrade_bought: The upgrade which was bought.
    :type upgrade_bought: UpgradeName
    """
    userID: str
    sessionID: int
    timestamp: datetime
    stage_number: int
    coins_spent: int
    upgrade_bought: UpgradeName


@dataclass(frozen=True)
class EndSession:
    """
    EndSession object represents an instance of an EndSession event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    """
    userID: str
    sessionID: int
    timestamp: datetime

    
@dataclass(frozen=True)
class SettingsChange:
    """
    SettingsChange object represents an instance of a 
    SettingsChange event.

    :param userID: UserID of user changing setting.
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param setting: The setting which was changed.
    :type setting: SettingName
    :param value: The new value to which the changed 
    setting was set to. 
    :type value: int
    :param justification: Optional justification for the settings
    change.
    :type justification: str 
    """
    userID: str
    timestamp: datetime
    setting: SettingName
    value: str
    justification: str

    
@dataclass(frozen=True)
class KillEnemy:
    """
    KillEnemy object represents an instance of a KillEnemy event.

    :param userID: Unique ID of the user.
    :type userID: str
    :param sessionID: Unique ID for the session.
    :type sessionID: int
    :param timestamp: Timestamp of the event. 
    Format: YYYY/MM/DD/HH/MM/SS.
    :type timestamp: datetime
    :param encounter_name: Name of the encounter player is 
    in at time of kill.
    :type encounter_name: EncounterName
    :param difficulty: Difficulty level of the encounter.
    :type difficulty: Difficulty
    :param stage_number: Current stage player is in at time of kill.
    :type stage_number: int
    :param enemy_type: Type of enemy which was killed.
    :type enemy_type: EnemyType
    """
    userID: str
    sessionID: int
    timestamp: datetime
    encounter_name: EncounterName
    difficulty: Difficulty
    stage_number: int
    enemy_type: EnemyType
