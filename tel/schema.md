# Definition of json schema
1) [List of valid event types](#list-of-valid-event-types)
2) [List of valid encounter names](#list-of-valid-encounter-names)
3) [List of valid upgrades](#list-of-valid-upgrade-names)
4) [List of valid enemy names](#list-of-valid-enemy-names)
5) [Event log examples](#example-logging-event-jsons)

# List of valid event types 
Here are the possible event types:
1) `NormalEncounterStart`
2) `NormalEncounterComplete`
3) `NormalEncounterFail`
4) `NormalEncounterRetry`
5) `BossEncounterStart`
6) `BossEncounterComplete`
7) `BossEncounterFail`
8) `BossEncounterRetry`
9) `GainCoin`
10) `BuyUpgrade`
11) `Quit`
12) `SettingsChange`
13) `KillEnemy`

# List of valid encounter names
To be defined at a later date.

# List of valid upgrade names
To be defined at a later date.

# List of valid enemy names
To be defined at a later date.

# Example logging event jsons

### 1) NormalEncounterStart
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "NormalEncounterStart"
}
```

### 8) BossEncounterRetry
```
{
    username: string,
    session_id: int,
    timestamp: YYYY/MM/DD/HH/MM/SS,
    encounter_name: string [must be in list of valid encounter names],
    event: "BossEncounterRetry"
}
```

### 9) GainCoin
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

### 10) BuyUpgrade
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
