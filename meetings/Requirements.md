# Features
## Java Application
#### 10 Levels of encounters
*As a player I want to have 10 levels worth of gameplay so the game is a gradual challenge to beat.*
#### Boss encounters on levels 3, 6, 9, with a final Boss on Level 10. 
*As a player I want big climatic boss fights to challenge myself, and provide milestones.*
#### 14 telemetry events including the specified fields
The system should implements the following telemetry events:
- Start Session
    - Username
    - Session ID
    - Timestamp
- Normal Encounter Start
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
- Normal Encounter Complete
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
    - Player HP Remaining
- Normal Encounter Fail
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
- Normal Encounter Retry
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
- Boss Encounter Start
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
- Boss Encounter Complete
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
- Boss Encounter Fail
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
- Boss Encounter Retry
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
- Gain Coin
    - Username
    - Session ID
    - Timestamp
    - Coins Gained
- Buy Upgrade
    - Username
    - Session ID
    - Timestamp
    - Upgrade Bought
- End Session
    - Username
    - Session ID
    - Timestamp
- Settings Change
    - Username
    - Session ID
    - Timestamp
    - Setting
    - Value
- Kill Enemy
    - Username
    - Session ID
    - Timestamp
    - Encounter Name
    - Enemy Type
*As a designer I want to see what areas of the game are too easy and what are too hard, so I can improve the game's balance.*
### Validate and write telemetry events to a JSON file
Telemetry events that are produced in an impossible order should not be written. Telemetry events with missing data should be recovered before writing where possible and otherwise discarded. 
*As a developer it is useful to have the data in a transportable format so other systems can interface with it.*
### Implements coins as a resource for buying upgrades
Coins should be obtained from completing encounters, with bosses giving more coins. Coins can then be spent at the shop to buy upgrades.
*As a developer I want a way to incentivize play and reward players, to encourage them to continue playing the game.*
### Upgrades are purchasable in the shop and can be sold for coins
The shop should provide a random assortment of available upgrades, with varying power levels and prices. They can be bought with coins and increase the power of the player's character. Already purchased upgrades can be sold in the shop for coins. 
*As a player I want ways to upgrade and modify my character so each run feels different and my character feels like they're getting stronger.*
### Health points are used to determine how close to death a player character is
*As a player I want to see how close to death my character, and my enemies are so I can determine what risks I can take.*
### Lives are used to determine how many times a character can die before having to restart their run
*As a player I want to have multiple lives so I have a safety net to learn the mechanics of the game so I can enjoy it more and play to higher levels.*
### Magic is used to fuel high power abilities
Magic should increase by a set amount each turn
## Python Application
## Project Report
## Individual Report
