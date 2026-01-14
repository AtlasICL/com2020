# Definition of json schema
1) [List of valid event types](#list-of-valid-event-types)
2) [List of valid encounter names](#list-of-valid-encounter-names)
3) [Event log examples](#example-logging-event-jsons)

# List of valid event types 
Here are the possible event types:
- `NormalEncounterStart`
- `NormalEncounterComplete`
- `NormalEncounterFail`
- `NormalEncounterRetry`
- `BossEncounterStart`
- `BossEncounterComplete`
- `BossEncounterFail`
- `BossEncounterRetry`
- `GainCoin`
- `BuyUpgrade`
- `Quit`
- `SettingsChange`
- `KillEnemy`

# List of valid encounter names
To be defined at a later date.

# Example logging event jsons

### NormalEncounterStart
```
{
    username: "harry1",
    session_id: 103871,
    timestamp: 2026/01/14/15/38/07,
    encounter_name: "wolf_fight",
    event: "NormalEncounterStart"
}
```

- username: string
- session_id: int
- timestamp: string (with specified format `YYYY/MM/DD/HH/MM/SS`)
- stage_name: string - must be one of the [defined encounter names](#list-of-valid-encounter-names)
- event: string - must be one of the [defined event types](#event-types)