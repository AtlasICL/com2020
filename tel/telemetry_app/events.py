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
    BOSS_ENCOUNTER_FAIL = "BOSS_ENCOUNTER_RETRY"
    BOSS_ENCOUNTER_RETRY = "BossEncounterRetry"

    GAIN_COIN = "GainCoin"
    BUY_UPGRADE = "BuyUpgrade"
    SETTINGS_CHANGE = "SettingsChange"
    KILL_ENEMY = "KillEnemy"


class EventParameter:
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
    SETTING_VALUE= "setting_value"
    ENEMY_TYPE= "enemy_type"


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
