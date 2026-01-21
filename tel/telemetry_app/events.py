from enum import Enum

class EventType(str, Enum):
    START_TELEMETRY = "StartTelemetry"
    SESSION_START = "SessionStart"
    END_SESSION = "EndSession"

    NORMAL_ENCOUNTER_START = "NormalEncounterStart"
    NORMAL_ENCOUNTER_COMPLETE = "NormalEncounterComplete"
    NORMAL_ENCOUNTER_FAIL = "NormalEncounterFail"
    NORMAL_ENCOUNTER_RETRY = "NormalEncounterRetry"

    BOSS_ENCOUNTER_START = "BossEncounterStart"
    BOSS_ENCOUNTER_COMPLETE = "BossEncounterComplete"
    BOSS_ENCOUNTER_FAIL = "BossEncounterFail"
    BOSS_ENCOUNTER_RETRY = "BossEncounterRetry"

    GAIN_COIN = "GainCoin"
    BUY_UPGRADE = "BuyUpgrade"
    SETTINGS_CHANGE = "SettingsChange"
    KILL_ENEMY = "KillEnemy"

class EventParameter():
    EVENT_TYPE = "event"
    USER_ID = "userID"
    SESSION_ID = "sessionID"
    TIMESTAMP = "timestamp"
    ENCOUNTER = "encounter_name"
    STAGE_NUMBER = "stage_number"
    LIVES_LEFT = "lives_left"
    COINS_GAINED = "coins_gained"
    UPGRADE_BOUGHT = "upgrade_bought"
    COINS_SPENT = "coins_spent"
    PLAYER_HP_REMAINING = "player_HP_remaining"
    SETTING = "setting"
    SETTING_VALUE = "setting_value"
    ENEMY_TYPE = "enemy_type"


# TODO: To be updated when encounter names are defined.
class EncounterName(str, Enum):
    ENCOUNTER_1 = "ENCOUNTER_1"
    ENCOUNTER_2 = "ENCOUNTER_2"
    BOSS_1 = "BOSS_1"


# TODO: To be updated when upgrade names are defined.
class UpgradeName(str, Enum):
    UPGRADE_1 = "UPGRADE_1"
    UPGRADE_2 = "UPGRADE_2"


# TODO: To be updated when enemy names are defined.
class EnemyType(str, Enum):
    ENEMY_TYPE_1 = "ENEMY_TYPE_1"
    ENEMY_TYPE_2 = "ENEMY_TYPE_2"
    ENEMY_TYPE_3 = "ENEMY_TYPE_3"

# TODO: To be updated when settings names are defined.
class SettingName(str, Enum):
    SETTING_1 = "SETTING_1"
    SETTING_2 = "SETTING_2"

class SessionStart:
    """
    SessionStart object represents an instance of a 
    SessionStart event.
    """
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str
    ):
        """ 
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp

    def __repr__(self):
        return f"""SessionStartObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}"""

class NormalEncounterStart:
    """
    NormalEncounterStart object represents an instance of a
    NormalEncounterStart event.
    """
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player is starting.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player is starting.
        :type stage_number: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

    def __repr__(self):
        return f"""NormalEncounterStartObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}"""

class NormalEncounterComplete:
    """
    NormalEncounterComplete object represents an instance of a
    NormalEncounterComplete event.
    """
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            player_HP_remaining: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player 
        has completed.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player has completed.
        :type stage_number: int
        :param player_HP_remaining: Number of HP points users 
        character has remaining.
        :type player_HP_remaining: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.player_HP_remaining = player_HP_remaining

    def __repr__(self):
        return f"""NormalEncounterCompleteObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}
            {self.player_HP_remaining=}"""

class NormalEncounterFail:
    """
    NormalEncounterFail object represents an instance of a
    NormalEncounterFail event.
    """
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player 
        has failed.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player has failed.
        :type stage_number: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

    def __repr__(self):
        return f"""NormalEncounterFailObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}"""

class NormalEncounterRetry:
    """
    NormalEncounterRetry object represents an instance of a
    NormalEncounterRetry event.
    """
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            lives_left: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player is retrying.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player is retrying.
        :type stage_number: int
        :param lives_left: Number of lives a player has 
        left for next try.
        :type lives_left: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.lives_left = lives_left

    def __repr__(self):
        return f"""NormalEncounterRetryObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}
            {self.lives_left=}"""
        
class BossEncounterStart:
    """
    BossEncounterStart object represents an instance of a
    BossEncounterStart event.
    """
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player is starting.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player is starting.
        :type stage_number: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

    def __repr__(self):
        return f"""BossEncounterStartObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}"""

class BossEncounterComplete:
    """
    BossEncounterComplete object represents an instance of a
    BossEncounterComplete event.
    """
    def __init__(self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            player_HP_remaining: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player 
        has completed.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player has completed.
        :type stage_number: int
        :param player_HP_remaining: Player HP remaining once 
        boss encounter is complete.
        :type player_HP_remaining: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.player_HP_remaining = player_HP_remaining

    def __repr__(self):
        return f"""BossEncounterObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}
            {self.player_HP_remaining=}"""
        
class BossEncounterFail:
    """
    BossEncounterFail object represents an instance of a
    BossEncounterFail event.
    """
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player has failed.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player has failed.
        :type stage_number: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number

    def __repr__(self):
        return f"""BossEncounterFailObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}"""

class BossEncounterRetry:
    """
    BossEncounterRetry object represents an instance of a
    BossEncounterRetry event.
    """
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            lives_left: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player is retrying.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player is retrying.
        :type stage_number: int
        :param lives_left: Lives left at end of encounter.
        :type lives_left: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.lives_left = lives_left

    def __repr__(self):
        return f"""BossEncounterRetryObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}
            {self.lives_left=}"""

class GainCoin:
    """
    GainCoin object represents an instance of a GainCoin event.
    """
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            encounter_name: EncounterName,
            stage_number: int,
            coins_gained: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player is in.
        :type encounter_name: EncounterName
        :param stage_number: Current stage number.
        :type stage_number: int
        :param coins_gained: Number of coins gained.
        :type coins_gained: int
        """        
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.coins_gained = coins_gained

    def __repr__(self):
        return f"""GainCoinObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}
            {self.coins_gained=}"""

class BuyUpgrade:
    """
    BuyUpgrade object represents an instance of an BuyUpgrade event.
    """
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str,
            stage_number: int,
            coins_spent: int,
            upgrade_bought: UpgradeName
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param stage_number: Current stage player is complete.
        :type stage_number: int
        :param coins_spent: Number of coins spent on 
        buying this upgrade.
        :type coins_spent: int
        :param upgrade_bought: The upgrade which was bought.
        :type upgrade_bought: UpgradeName
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.stage_number = stage_number
        self.coins_spent = coins_spent
        self.upgrade_bought = upgrade_bought

    def __repr__(self):
        return f"""BuyUpgradeObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.stage_number=}
            {self.coins_spent=}
            {self.upgrade_bought=}"""

class EndSession:
    """
    EndSession object represents an instance of an EndSession event.
    """
    def __init__(
            self,
            userID: int,
            sessionID: int,
            timestamp: str
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp

    def __repr__(self):
        return f"""EndSessionObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}"""

class SettingsChange:
    """
    SettingsChange object represents an instance of a 
    SettingsChange event.
    """
    def __init__(
        self,
        userID: int,
        sessionID: int,
        timestamp: str,
        setting: SettingName,
        value: int
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param setting: The setting which was changed.
        :type setting: SettingName
        :param value: The new value to which the changed 
        setting was set to. 
        :type value: int
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.setting = setting
        self.value = value

    def __repr__(self):
        return f"""SettingsChangeObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.setting=}
            {self.value=}"""

class KillEnemy:
    """
    KillEnemy object represents an instance of a KillEnemy event.
    """
    def __init__(
        self,
        userID: int,
        sessionID: int,
        timestamp: str,
        encounter_name: EncounterName,
        stage_number: int,
        enemy_type: EnemyType
    ):
        """
        :param userID: Unique ID of the user.
        :type userID: int
        :param sessionID: Unique ID for the session.
        :type sessionID: int
        :param timestamp: Timestamp of the event. 
        Format: YYYY/MM/DD/HH/MM/SS.
        :type timestamp: str
        :param encounter_name: Name of the encounter player is 
        in at time of kill.
        :type encounter_name: EncounterName
        :param stage_number: Current stage player is in at time of kill.
        :type stage_number: int
        :param enemy_type: Type of enemy which was killed.
        :type enemy_type: EnemyType
        """
        self.userID = userID
        self.sessionID = sessionID
        self.timestamp = timestamp
        self.encounter_name = encounter_name
        self.stage_number = stage_number
        self.enemy_type = enemy_type

    def __repr__(self):
        return f"""KillEnemyObject
            {self.userID=}
            {self.sessionID=}
            {self.timestamp=}
            {self.encounter_name=}
            {self.stage_number=}
            {self.enemy_type=}"""

