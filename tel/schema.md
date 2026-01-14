# Definition of json schema
- [Definition of json schema](#definition-of-json-schema)
- [List of valid event types](#list-of-valid-event-types)
- [List of valid encounter names](#list-of-valid-encounter-names)
- [List of valid upgrade names](#list-of-valid-upgrade-names)
- [List of valid enemy names](#list-of-valid-enemy-names)
- [List of valid settings](#list-of-valid-settings)
- [Example logging event jsons](#example-logging-event-jsons)
    - [1) SessionStart](#1-sessionstart)
    - [2) NormalEncounterStart](#2-normalencounterstart)
    - [3) NormalEncounterComplete](#3-normalencountercomplete)
    - [4) NormalEncounterFail](#4-normalencounterfail)
    - [5) NormalEncounterRetry](#5-normalencounterretry)
    - [6) BossEncounterStart](#6-bossencounterstart)
    - [7) BossEncounterComplete](#7-bossencountercomplete)
    - [8) BossEncounterFail](#8-bossencounterfail)
    - [9) BossEncounterRetry](#9-bossencounterretry)
    - [10) GainCoin](#10-gaincoin)
    - [11) BuyUpgrade](#11-buyupgrade)
    - [12) EndSession](#12-endsession)
    - [13) SettingsChange](#13-settingschange)
    - [14) KillEnemy](#14-killenemy)

# List of valid event types 
Here are the possible event types:
1) `startSession`
2) `NormalEncounterStart`
3) `NormalEncounterComplete`
4) `NormalEncounterFail`
5) `NormalEncounterRetry`
6) `BossEncounterStart`
7) `BossEncounterComplete`
8) `BossEncounterFail`
9) `BossEncounterRetry`
10) `GainCoin`
11) `BuyUpgrade`
12) `EndSession`
13) `SettingsChange`
14) `KillEnemy`

# List of valid encounter names
To be defined at a later date.

# List of valid upgrade names
To be defined at a later date.

# List of valid enemy names
To be defined at a later date.

# List of valid settings
To be defined at a later date.

# Example logging event jsons

### 1) SessionStart
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    event: "SessionStart"
}
```

### 2) NormalEncounterStart
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "NormalEncounterStart"
}
```

### 3) NormalEncounterComplete
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "NormalEncounterComplete",
    playerHPRemaining: int, 
}
```

### 4) NormalEncounterFail
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "NormalEncounterFail"
}
```

### 5) NormalEncounterRetry
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "NormalEncounterRetry"
}
```

### 6) BossEncounterStart
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "BossEncounterStart"
}
```

### 7) BossEncounterComplete
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "BossEncounterComplete"
}
```

### 8) BossEncounterFail
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "BossEncounterFail"
}
```

### 9) BossEncounterRetry
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "BossEncounterRetry"
}
```

### 10) GainCoin
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "GainCoin",
    coins_gained: int
}
```

### 11) BuyUpgrade
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "BuyUpgrade",
    upgrade_bought: string [must be in list of upgrades]
}
```

### 12) EndSession
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    event: "EndSession"
}
```

### 13) SettingsChange
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    setting: string [must be in list of valid settings]
    value: int
}
```

### 14) KillEnemy
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    enemy_type: string
}
```