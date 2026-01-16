# Features
## Java Application
#### 10 Levels of encounters
Each stage should draw an encounter of suitable difficulty from a pool of pre-made encounters.

*As a player I want to have 10 levels worth of gameplay so the game is a gradual challenge to beat.*
#### Boss encounters on levels 3, 6, 9, with a final Boss on Level 10. 
*As a player I want big climatic boss fights to challenge myself, and provide milestones.*
#### Telemetry events including the specified fields
The system should implements the following telemetry events:
- Start Session
    - User ID
    - Session ID
    - Timestamp
- Run Start
    - User ID
    - Session ID
    - Timestamp 
    - Difficulty
- Normal Encounter Start
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Stage
- Normal Encounter Complete
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Player HP Remaining
    - Stage
- Normal Encounter Fail
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Stage
- Normal Encounter Retry
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Stage
- Boss Encounter Start
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Stage
- Boss Encounter Complete
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Stage
- Boss Encounter Fail
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Stage
- Boss Encounter Retry
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Stage
- Gain Coin
    - User ID
    - Session ID
    - Timestamp
    - Coins Gained
    - Stage
- Buy Upgrade
    - User ID
    - Session ID
    - Timestamp
    - Upgrade Bought
    - Stage
- End Session
    - User ID
    - Session ID
    - Timestamp
- Settings Change
    - User ID
    - Session ID
    - Timestamp
    - Setting
    - Value
- Kill Enemy
    - User ID
    - Session ID
    - Timestamp
    - Encounter Name
    - Enemy Type
    - Stage 

Note: Some telemetry events do not include stage information because they are sourced outside a stage (e.g. settings change)
*As a designer I want to see what areas of the game are too easy and what are too hard, so I can improve the game's balance.*
### Validate and write telemetry events to a JSON file
Telemetry events that are produced in an impossible order should not be written. Telemetry events with missing data or with invalid data should be recovered before writing where possible and otherwise discarded. 

*As a developer it is useful to have the data in a transportable format so other systems can interface with it.*
### Implements coins as a resource for buying upgrades
Coins should be obtained from completing encounters, with bosses giving more coins. Coins can then be spent at the shop to buy upgrades.

*As a developer I want a way to incentivize play and reward players, to encourage them to continue playing the game.*
### Upgrades are purchasable in the shop and can be sold for coins
The shop should provide a random assortment of available upgrades, with varying power levels and prices. They can be bought with coins and increase the power of the player's character. Already purchased upgrades can be sold in the shop for coins. 

*As a player I want ways to upgrade and modify my character so each run feels different and my character feels like they're getting stronger.*
### Health points are used to determine how close to death a player character is
Player characters should have a maximum health points and a current health points. When their current health points reaches 0 they should die. 

*As a player I want to see how close to death my character, and my enemies are so I can determine what risks I can take.*
### Lives are used to determine how many times a character can die before having to restart their run
Player characters should start runs with some value of lives and each time they die their number of lives should reduce by 1. When they have 0 lives they restart the game rather than the encounter. 

*As a player I want to have multiple lives so I have a safety net to learn the mechanics of the game so I can enjoy it more and play to higher levels.*
### Magic is used to fuel high power abilities
Magic should increase by a set amount each turn, and can be spent to use high power abilities.

*As a developer I want a resource that allows me to make strong abilities without breaking the game, so i can add additional strategy and cinematic moments to the game.*
### The game should have the gameplay loop described below
Start: Login screen. Log in valid -> Title Screen, Log in invalid -> Login Screen, Create new user -> User Creation Screen.

User Creation Screen. Invalid account details -> User Creation Screen, Valid account details -> Title Screen.

Title Screen. Start game (with difficulty option) -> Encounter 1, Edit settings -> Settings

Settings. Save changes -> Title Screen.

Encounter 1. Die and out of lives -> Title Screen, Die with lives -> Encounter , Kill all enemies -> Shop 1.

Shop 1. Leave shop -> Encounter 2. 

Encounter 2. Die and out of lives -> Title Screen, Die with lives -> Encounter 2, Kill all enemies -> Shop 2.

Shop 2. Leave shop -> Encounter 3.

Repeat above to Encounter/shop 9.

Encounter 10. Die and out of lives -> Title Screen, Die with lives -> Encounter 10, Kill all enemies -> Win Screen.

Win Screen. Restart -> Title Screen. 

*As a developer I want a simple gameplay loop so I can extend it and implement mechanics around it.*
### Player settings
Players should have the following settings
- Whether they send telemetry (default on, communicated in the title screen)

*As a player, I would like the option to opt-out of telemetry as privacy is important to me.*
### End Screen
The game should display an end screen when a run ends (either by winning or running out of lives). This should include the following information:
- Run Time
- Number of deaths
- Coins
- Upgrades
- Completion: Either the encounter they lost at, or complete if they won
### Difficulty settings
The game should include easy, balanced, and hard difficulties.

*As a player, I want a range of difficulty settings so the game can be a challenge to me while still being fun.*
### Design parameters
The game should have the following parameters that designers can tweak:
- Enemy health point multiplier
- Player maximum health points (and upgrades that increase their maximum health points) multiplier
- Upgrade price multiplier 
- Enemy damage multiplier
- Starting lives
- Magic cost multiplier: multiplies the cost of magic using abilities
- Maximum magic amount
- Magic regeneration rate: amount gained each turn 

These should be saved in a configuration file, and be per difficulty. Changes to them she be able to be made in game by designers, and update the game when it is restarted. 

*As a designer I want many different parameters to alter, to allow me to fine tune the balance of the game.*
### Design log
When a change to the game's balancing parameters is made, this should be logged in a design log with a reason that links back to dashboard evidence. There should also be 30 seeded decisions within this log (some good, some flawed). 

*As a lead designer, I want to see what changes other designers are making and why to remain up to date with the project.*
### User authentication and roles
The game should authenticate it's users, requiring a username and password. The password should be hashed and salted before storage. User should have exclusively 1 of the following roles:
- Player: can play the game, but cannot modify it's difficulty parameters or read telemetry.
- Designer: can play the game and modify it's difficulty parameters as well as read telemetry.
- Developer: same as a designer, can modify the application as well

*As a designer, I want to ensure my account is secure so no one else can use my privileges to damage the game.*
### Java automated tests 
Between this and the python application, there must be at least 15 automated tests
Every method of every class should have at least 1 method for testing it. These should cover the following areas:
- Telemetry event validation
- Data storage
- Changing balancing parameters 
- Simulation mode
- Writing and reading from the decision log

*As a designer I want to ensure the program works correctly, so I need testing to provide this assurance.*
### Java manual tests
Between this and the python application, there must be at least 8 manual tests, with evidence of their success.
- Create an account
- Resetting a password 
- Log in as a player
- Log in as a designer
- Beat the game as a player
- Adjust a parameter as a designer and see the result reflected in game
- Lose all lives and go back to the start as a developer

Should include in test evidence expected results, success and failure cases, and screenshots/logs of them. 

*As a designer I want to make sure all paths through the system work as intended so my balancing changes are not affected by the system working incorrectly.*
### Automated player agent
An automated player agent that can simulate runs of the game to provide feedback on design changes.

*As a designer I would like instant feedback on my design decisions to help me evaluate them.*
### Java accessible design
The software should:
- Have high contrast between UI elements
- Allow tab and shift-tab navigation of elements
- Have a resizable GUI that scales with window size and works on most desktop environments
- Have sufficiently large GUI elements that they are clearly visible, including text.  

*As a player I require software to have good accessability in order for it to be playable to me.*
## Python application
### Read in telemetry from the JSON file that the Java application writes to
*As a designer, I want to be able to view the telemetry data so I can use it to influence design decisions*
### Clean read-in telemetry
The system should detect malformed telemetry and attempt to recover it where possible, and otherwise ignore it. This should include telemetry thats in an impossible order, has missing fields or out-of-range values.  

*As a designer, I want to see as much telemetry as possible, while still keeping it accurate, so recoverability of the data is important to me.*
### Dashboard views of the telemetry 
The program should be able to display the telemetry information as follows:
- Funnel View: Show the completion rate of each stage, and its failure rate
- Difficulty Spikes: highlight stages with high fail rates and completion times
- Progression Curve: show the time to complete throughout the game and the resource accumulation throughout the game 
- Fairness Indicators: compare between 2+ play styles for the same encounter:
    - Defensive vs Aggressive
    - Fast vs Slow
- Comparison Mode: compare easy, balanced, and hard runs against each other on key metrics:
    - Completion time
    - Number of retries
    - Average encounter end health points

*As a designer I want several perspectives on the game so I can see it from different angles to inform my design decisions.*
### CSV Export of telemetry
The data should be able to be exported as a CSV file with a specified format. 

*As a designer I would like to be able to export the data as a CSV so I can view and process it in other applications, improving my productivity.*
### Telemetry sample set
1500 seeded telemetry events, spread across 80 sessions with 40 different users. This should be across all 10 levels, on each difficulty setting. 
Must include 150 malformed telemetry events.

*As a tester I want a sample telemetry set I can use to test the application functions correctly.*
### Rule based suggestion system
The application should check the data, and if it matches one of the 6+ predefined rules it will make a design recommendation to the designer about a potential change to the game that would improve it.

*As a designer it is helpful to get design suggestions as these can be implemented and improve the game and player experience.*
### Python automated tests
The application should have automated tests testing each class and method. These should cover the following areas:
- Telemetry event validation
- Data retrieval 
- Dashboard computations
- Rule-based suggestions
- Exports of data as CSV

*As a developer I want to make sure my application produces the correct results as to give my client the best experience.*
### Python manual tests
The application should include the following manual tests with evidence:
- Attempting to log in as a player
- Logging in as a designer 
- Viewing each dashboard view with the seeded telemetry

This should contain expected results, as well as success and failure cases and screenshots/logs of completed runs.

*As a designer I want to know that the application works as I want so I can be as productive with it as possible and use it to my advantage.*
### Python accessible design
The software should:
- Have high contrast between UI elements
- Allow tab and shift-tab navigation of elements
- Have a resizable GUI that scales with window size and works on most desktop environments
- Have sufficiently large GUI elements that they are clearly visible, including text.  

*As a user I require software to have good accessability in order to be able to use it effectively.*
## Project Report
### A summary of the report
*As an evaluator a brief overview of the report will prepare me for what its about.*
### List of requirements prioritized for that sprint  
*As an evaluator I want to see what the team has worked on during each sprint*
### Design architecture
This should include a overall diagram detailing how the Java and python application work together. It should also include UML for both the Java and Python applications, and a written explanation of what each class is responsible for. 

*As an evaluator I want to see what components are responsible for what to better understand the system.*
### Telemetry schema
*As a developer I want to see what the schema is so I can interface with it.*
### Evidence of initial analysis 
*As an evaluator I want to see that the requirements have been thoroughly considered.*
### Sprint plan for both sprints
*As an evaluator I want to see that the project has been planned out, and is progressing well.*
### Success measures
Report how well the system scores on the following success measures, as well as what methodology was used to produce these scores and any limitations with that methodology:
- Detection validity: do the difficulty spike detection rules correctly identify difficulty spikes in the seeded data?
- Fairness Analysis: do different play styles have different outcomes? Are those differences explained and mitigations proposed by the system?
- Balancing Effectiveness: do the suggested parameter changes reduce spikes in difficulty, in the simulation or in pilot runs?
- Usability: can designers locate issues and make informed design changes within a short timeframe?

*As a developer I want to evaluate the project to determine what can be improved in further iterations, and what has been learnt from the creation of this project.*
### Fairness discussion
A discussion on the game's fairness and on any unintended consequences of balancing changes or of the systems within the game.

*As a designer I want to see a discussion around the fairness of the game to identify what can be improved with further design changes.*
### Ethical and Legal considerations of the project
This should consider the implication of the following:
- Privacy of the telemetry
- Consent for the capture of telemetry
- Disclosure telemetry is being captured
- Accessability of the software
- Intellectual property of the software, and their implications on it's licensing 

*As an evaluator I want to know that the team has considered the ethics and legality of their software so I can trust this software to be safe.*
### A software bill of materials 
Should contain all components of the both the Python and Java application, and the following properties of each component:
- It's license 
- It's developer
- Where it's obtained from
- What cost model it uses
- What version the software makes use of

*As a developer I want to know exactly what requirements the software has, so I can ensure supply chain security and maintain the software.*
## Handover pack
A handover pack should be included detailing the following information to maintainers and developers of the system once it's handed over to them:
- Instructions for deploying both softwares
- Operations that can be performed in each software, and how to do so
- Maintenance guidelines on the architecture of the system, what each class is responsible for, how to extend classes and add additional functionality, etc.
- How to run the automated tests for both applications

*As a maintainer it's important for me to have a handover pack so I can understand how to maintain the software and use it.*
## Individual Report (Individual)
### Role and contribution
What roles the person undertook throughout the project, and what they've contributed to it.

*As an evaluator I want to see that each person has contributed to the team, and see what they've contributed to it.*
### What was learnt
3 technical or professional learning points linked with the module outcomes, that the writer has learned in working on this project.

*As a developer it's important to reflect on what I've learnt to show my own growth within the subject.*
### What challenges the writer has faced
Must include 1 technical challenge and 1 teamwork/process challenge. Should explain what changed as a result of these challenges.

*As a developer it's important to acknowledge what challenges ive faced so when I face them again i know what changes need to be made to overcome them.*
### What ethical/legal considerations you made in relation to what you worked on
*As an evaluator it's important to me that developers are aware of the legal and ethical implications of their work to ensure their software is ethical and safe.*
### An AI-minimal compliance statement
It should state what AI was used for (checking of spelling and grammar), and that the writer adhered to the requirements of this brief. 

*As an evaluator it's important that developers don't use AI to generate their code as then their code shows their level of understanding.*
# Implementation Plan
## Sprint 1
### Must
#### Java application must contain a user login page
It will allow players, designers and developers as specified [here](#user-authentication-and-roles)
#### Java application must contain 2 stages
Each stage must have at least 1 encounter it can pull from. See [here](#10-levels-of-encounters) for more information.
#### Java application must implement 6 telemetry types
See [here](#telemetry-events-including-the-specified-fields) for list of telemetry events. They should be sent during the runtime of the system in all completed stages.
#### Java application must implement 1 balancing parameter
See parameters [here](#design-parameters)
#### Java application must implement player-agent simulation
[More information](#automated-player-agent)
#### Python application must read 6 telemetry types
See [here](#telemetry-events-including-the-specified-fields) for list of telemetry events. 
#### Python application should display 3 dashboard views
See views [here](#dashboard-views-of-the-telemetry)
#### Between the Python and Java applications there must be at least 5 automated tests
[Python tests](#python-automated-tests)

[Java tests](#java-automated-tests)
#### Between the Python and Java applications there must be 1 manual test
This should include fail/success cases, expected results, and screenshots/logs of completion in test_evidence.pdf.

[Python tests](#python-manual-tests)

[Java tests](#java-manual-tests)
#### Deployment Guide
Containing information on running the tests
#### Prototype report
[Summary](#a-summary-of-the-report)

[Requirements](#list-of-requirements-prioritized-for-that-sprint)

[Architecture](#design-architecture)

[Telemetry schema](#telemetry-schema)

[Initial evaluation evidence](#evidence-of-initial-analysis)

[Sprint plan for sprint 2](#sprint-plan-for-both-sprints)

[Ethical and legal considerations](#ethical-and-legal-considerations-of-the-project)

[Software bill of materials](#a-software-bill-of-materials)
#### Coins and Upgrades systems
[Coins](#implements-coins-as-a-resource-for-buying-upgrades)

[Upgrades can be bought](#upgrades-are-purchasable-in-the-shop-and-can-be-sold-for-coins)
#### Implement health points and lives
[Health points](#health-points-are-used-to-determine-how-close-to-death-a-player-character-is)

[Lives](#lives-are-used-to-determine-how-many-times-a-character-can-die-before-having-to-restart-their-run)
#### 2 Normal Encounters
#### 1 Normal Enemy
### Should
#### Java application should contain 3 stages
Stage 3 should be a boss fight, in line with [here](#10-levels-of-encounters)
#### Java application should validate telemetry before storing it
[More information](#validate-and-write-telemetry-events-to-a-json-file)
#### Python application should clean telemetry before displaying it
[More information](#clean-read-in-telemetry)
#### 300 Seeded telemetry events
Including 30 anomalous, across 16 sessions and 8 users. More information [here](#telemetry-sample-set)
#### 4+ Normal Encounters
#### 2+ Normal Enemies 
#### 1 Boss Encounter
#### Magic system
[More information](#magic-is-used-to-fuel-high-power-abilities)
#### Accessible design
[Python](#python-accessible-design)

[Java](#java-accessible-design)
#### Different difficulties
[More information](#difficulty-settings)
### Could 
#### Decision log
[More information](#decision-log)
#### 12 telemetry events
[More information](#telemetry-events-including-the-specified-fields)
#### 15+ automated tests
[Java information](#java-automated-tests)

[Python information](#python-automated-tests)
#### Provide Handover pack
[More information](#handover-pack)
#### 2+ Boss Encounters
#### 6+ Normal Enemies
### Wont
#### All telemetry events
#### 1500 seeded telemetry events
#### 10 stages
#### 6 dashboard views
#### Rule based suggestions
#### Multiplayer
#### Encounter builder
#### Dynamic Encounter generation
#### Dynamic Enemy generation
#### Centralized scoring
#### An additional resource alongside magic
#### Custom upgrade builder
#### More than 10 levels
#### Dynamic Difficulty
#### Mid run difficulty adjustment
## Sprint 2
### Must 
#### Implement 10 levels including bosses
[Level specification](#10-levels-of-encounters)

[Boss specification](#boss-encounters-on-levels-3-6-9-with-a-final-boss-on-level-10)
#### Implement health and lives
[Health points](#health-points-are-used-to-determine-how-close-to-death-a-player-character-is)

[Lives](#lives-are-used-to-determine-how-many-times-a-character-can-die-before-having-to-restart-their-run)
#### Implement coins and upgrades
[Coins](#implements-coins-as-a-resource-for-buying-upgrades)

[Upgrades can be bought](#upgrades-are-purchasable-in-the-shop-and-can-be-sold-for-coins)
#### 12 Telemetry events
[Events](#telemetry-events-including-the-specified-fields)

The java application should validate and write them to a JSON file ([Specification](#validate-and-write-telemetry-events-to-a-json-file)). The Python application should read and clean them, then display them([reading](#read-in-telemetry-from-the-json-file-that-the-java-application-writes-to), [cleaning](#clean-read-in-telemetry)). 
#### Authenticated Python and Java applications
[More Information](#user-authentication-and-roles)
#### Magic fuels abilities
[More information](#magic-is-used-to-fuel-high-power-abilities)
#### 20+ Upgrades
#### 7+ Bosses Encounters
2 choices for level 3, 2 choices for level 6, 2 choices for level 9 and the final boss on level 10. 
#### 12+ Normal Encounters
4 choices for levels 1 and 2, 4 choices for levels 4 and 5, and 4 choices for levels 7 and 8. 
#### All stages of the gameplay loop
[Loop information](#the-game-should-have-the-gameplay-loop-described-below)

[End screen information](#end-screen)
#### Game settings
[Settings information](#player-settings)
#### Difficulty modes
[More information](#difficulty-settings)
#### 8+ Design parameters
[More information](#design-parameters)
#### Design Decision Log
[More information](#design-log)
#### 15+ Automated tests
[Java](#java-automated-tests)

[Python](#python-automated-tests)
#### 8+ Manual tests
[Java](#java-manual-tests)

[Python](#python-manual-tests)
#### Simulation mode
[More information](#automated-player-agent)
#### Accessible design
[Java](#java-accessible-design)

[Python](#python-accessible-design)
#### 6+ dashboard views
[More information](#dashboard-views-of-the-telemetry)
#### CSV telemetry export
[More information](#csv-export-of-telemetry)
#### Seeded telemetry set
[More information](#telemetry-sample-set)
#### Python application should give rule-based suggestions
[More information](#rule-based-suggestion-system)
#### Project report
[Summary](#a-summary-of-the-report)

[Requirements](#list-of-requirements-prioritized-for-that-sprint)

[Architecture](#design-architecture)

[Telemetry schema](#telemetry-schema)

[Initial evaluation evidence](#evidence-of-initial-analysis)

[Sprint plan for sprint 2](#sprint-plan-for-both-sprints)

[Success measures](#success-measures)

[Discussion on fairness](#fairness-discussion)

[Ethical and legal considerations](#ethical-and-legal-considerations-of-the-project)

[Software bill of materials](#a-software-bill-of-materials)
#### A handover pack
[More information](#handover-pack)
#### Individual reports
[Role and contribution](#role-and-contribution)

[Learning](#what-was-learnt)

[Challenges](#what-challenges-the-writer-has-faced)

[Ethical/legal considerations](#what-ethicallegal-considerations-you-made-in-relation-to-what-you-worked-on)

[AI-minimal compliance statement](#an-ai-minimal-compliance-statement)
#### 6+ Normal Enemy types
### Should
#### All Telemetry events
[Events](#telemetry-events-including-the-specified-fields)
#### 9+ Normal Enemy Types
#### 18+ Normal Encounters
6 to pull from for each pair of levels. (1&2, 4&5, 7&8)
### Could
#### Upgrades can be sold
[More information](#upgrades-are-purchasable-in-the-shop-and-can-be-sold-for-coins)
#### 30+ upgrades
### Wont 
#### Multiplayer
#### Encounter builder
#### Dynamic Encounter generation
#### Dynamic Enemy generation
#### Centralized scoring
#### An additional resource alongside magic
#### Custom upgrade builder
#### More than 10 levels
#### Dynamic Difficulty
#### Mid run difficulty adjustment