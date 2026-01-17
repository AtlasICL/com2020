from enum import Enum

class EventType(str, Enum):
    StartTelemetry = "StartTelemetry"
    SessionStart = "SessionStart"
    EndSession = "EndSession"

    NormalEncounterStart = "NormalEncounterStart"
    NormalEncounterComplete = "NormalEncounterComplete"
    NormalEncounterFail = "NormalEncounterFail"
    NormalEncounterRetry = "NormalEncounterRetry"

    BossEncounterStart = "BossEncounterStart"
    BossEncounterComplete = "BossEncounterComplete"
    BossEncounterFail = "BossEncounterFail"
    BossEncounterRetry = "BossEncounterRetry"

    GainCoin = "GainCoin"
    BuyUpgrade = "BuyUpgrade"
    SettingsChange = "SettingsChange"
    KillEnemy = "KillEnemy"


# TODO: To be updated when encounter names are defined.
class EncounterName(str, Enum):
    Encounter1 = "ENCOUNTER_1"
    Encounter2 = "ENCOUNTER_2"
    Boss1 = "BOSS_1"


# TODO: To be updated when upgrade names are defined.
class UpgradeName(str, Enum):
    Upgrade1 = "UPGRADE_1"
    Upgrade2 = "UPGRADE_2"


# TODO: To be updated when enemy names are defined.
class EnemyType(str, Enum):
    EnemyType1 = "ENEMY_TYPE_1"
    EnemyType2 = "ENEMY_TYPE_2"
    EnemyType3 = "ENEMY_TYPE_3"

# TODO: To be updated when settings names are defined.
class SettingName(str, Enum):
    Setting1 = "SETTING_1"
    Setting2 = "SETTING_2"
