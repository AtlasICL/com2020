# Requirements 
## Must (Before CA 1)

- Have a java video game application that
    - has 2 or more levels, one of which contains a boss fight
    - must log the following telemetry (6 or more of these)
        - EncounterStart(Boss: bool)
        - NormalEncounterComplete(PlayerHPRemaining: int)
        - NormalEncounterFail()
        - NormalEncounterRetry()
        - GainCoins(NumberOfCoins: int)
        - BuyUpgrade(UpgradeName: Upgrade(Enum), Cost: int)
        - Quit()
        - SettingsChange(Setting: Setting(Enum), Value: str)
        - KillEnemy(EnemyType: EnemyType(Enum))
    - all telemetry must contain the following
        - Username: str
        - SessionID: int
        - Timestamp: (ss:mm:hh dd/mm/yyyy)
        - StageName: Stage(Enum)
    - have the following mechanics
        - coins
        - health points
        - lives 
    - 
- Have a python application that 
    - 
## Should (Before CA 2)
## Could 
## Wont 