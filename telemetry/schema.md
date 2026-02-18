# Definition of json schema
- [Definition of json schema](#definition-of-json-schema)
- [List of valid event types](#list-of-valid-event-types)
  - [List of valid difficulties](#list-of-valid-difficulties)
  - [List of valid encounter names](#list-of-valid-encounter-names)
  - [List of valid upgrade names](#list-of-valid-upgrade-names)
  - [List of valid enemy names](#list-of-valid-enemy-names)
  - [List of valid settings](#list-of-valid-settings)
  - [Example logging event jsons](#example-logging-event-jsons)
    - [1) StartSession](#1-startsession)
    - [2) NormalEncounterStart](#2-normalencounterstart)
    - [3) NormalEncounterComplete](#3-normalencountercomplete)
    - [4) NormalEncounterFail](#4-normalencounterfail)
    - [5) BossEncounterStart](#5-bossencounterstart)
    - [6) BossEncounterComplete](#6-bossencountercomplete)
    - [7) BossEncounterFail](#7-bossencounterfail)
    - [8) GainCoin](#8-gaincoin)
    - [9) BuyUpgrade](#9-buyupgrade)
    - [10) EndSession](#10-endsession)
    - [11) SettingsChange](#11-settingschange)
    - [12) KillEnemy](#12-killenemy)


# List of valid event types 
Here are the possible event types:
1) `StartSession`
2) `NormalEncounterStart`
3) `NormalEncounterComplete`
4) `NormalEncounterFail`
5) `BossEncounterStart`
6) `BossEncounterComplete`
7) `BossEncounterFail`
8)  `GainCoin`
9)  `BuyUpgrade`
10) `EndSession`
11) `SettingsChange`
12) `KillEnemy`

## List of valid difficulties
1) `Easy`
2) `Normal`
3) `Hard`

## List of valid encounter names
1) `GoblinEncounter`
2) `FishmanEncounter`

## List of valid upgrade names
1) `PhysicalDamageResistance`
2) `FireDamageResistance`
3) `WaterDamageResistance`
4) `ThunderDamageResistance`
5) `ImprovedPhysicalDamage`
6) `ImprovedFireDamage`
7) `ImprovedWaterDamage`
8) `ImprovedThunderDamage`
9) `SlashUnlock`
10) `AbsolutePulseUnlock`
11) `WaterJetUnlock`
12) `FireBallUnlock`
13) `ThunderStormUnlock`

## List of valid enemy names
1) `Goblin`
2) `Fishman`

## List of valid settings
1) `MaxStageReached`
2) `PlayerMaxHealth`
3) `EnemyDamageMultiplier`
4) `StartingLives`
5) `MaxMagic`
6) `MagicRegenRate`
7) `ShopItemCount`

## Example logging event jsons

### 1) StartSession
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    event: "StartSession"
    difficulty: string [must be in list of valid difficulties]
}
```

### 2) NormalEncounterStart
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    event: "NormalEncounterStart"
}
```

### 3) NormalEncounterComplete
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    event: "NormalEncounterComplete",
    player_HP_remaining: int
}
```

### 4) NormalEncounterFail
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    event: "NormalEncounterFail",
    lives_left: int
}
```

### 5) BossEncounterStart
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    event: "BossEncounterStart"
}
```

### 6) BossEncounterComplete
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    event: "BossEncounterComplete"
    player_HP_remaining: int
}
```

### 7) BossEncounterFail
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    event: "BossEncounterFail",
    lives_left: int
}
```

### 8) GainCoin
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    event: "GainCoin",
    coins_gained: int
}
```

### 9) BuyUpgrade
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    stage_number: int,
    event: "BuyUpgrade",
    upgrade_bought: string [must be in list of upgrades],
    coins_spent: int
}
```

### 10) EndSession
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    event: "EndSession"
}
```

### 11) SettingsChange
```
{
    userID: string, 
    timestamp: YYYY/MM/DD/HH/MM/SS,
    setting: string [must be in list of valid settings],
    setting_value: string,
    event: "SettingsChange"
}
```

### 12) KillEnemy
```
{
    userID: string,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    enemy_type: string [must be in list of valid enemy names],
    event: "KillEnemy"
}
```