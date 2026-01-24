# Definition of json schema
- [List of valid event types](#list-of-valid-event-types)
- [List of valid difficulties](#list-of-valid-difficulties)
- [List of valid encounter names](#list-of-valid-encounter-names)
- [List of valid upgrade names](#list-of-valid-upgrade-names)
- [List of valid enemy names](#list-of-valid-enemy-names)
- [List of valid settings](#list-of-valid-settings)
- [Example logging event jsons](#example-logging-event-jsons)
    - [1) SessionStart](#1-sessionstart)
    - [2) NormalEncounterStart](#2-normalencounterstart)
    - [3) NormalEncounterComplete](#3-normalencountercomplete)
    - [4) NormalEncounterFail](#4-normalencounterfail)
    - [5) BossEncounterStart](#5-bossencounterstart)
    - [6) BossEncounterComplete](#7-bossencountercomplete)
    - [7) BossEncounterFail](#8-bossencounterfail)
    - [8) GainCoin](#10-gaincoin)
    - [9) BuyUpgrade](#11-buyupgrade)
    - [10) EndSession](#12-endsession)
    - [11) SettingsChange](#13-settingschange)
    - [12) KillEnemy](#14-killenemy)


# List of valid event types 
Here are the possible event types:
1) `SessionStart`
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
To be defined at a later date.

## List of valid upgrade names
To be defined at a later date.

## List of valid enemy names
To be defined at a later date.

## List of valid settings
To be defined at a later date.

## Example logging event jsons

### 1) SessionStart
```
{
    userID: int,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    event: "SessionStart"
    difficulty: string [must be in list of valid difficulties]
}
```

### 2) NormalEncounterStart
```
{
    userID: int,
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
    userID: int,
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
    userID: int,
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
    userID: int,
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
    userID: int,
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
    userID: int,
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
    userID: int,
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
    userID: int,
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
    userID: int,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    event: "EndSession"
}
```

### 11) SettingsChange
```
{
    userID: int,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    setting: string [must be in list of valid settings],
    setting_value: string,
    event: "SettingsChange"
}
```

### 12) KillEnemy
```
{
    userID: int,
    sessionID: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    difficulty: string [must be in list of valid difficulties],
    stage_number: int,
    enemy_type: string,
    event: "KillEnemy"
}
```