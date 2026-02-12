## Format
Backlog contains things we need to do. Requirement is then split into user stories with acceptance criteria.The criteria are split into the following sections: report for documentation, python for the python app, and java for the java app.
## Report
### Prototype Report
#### Executive Summary
#### Problem Framing
The monitoring and maintenance of a game is essential for ensuring that players have an enjoyable experience, and
that their engagement remains intact. However, game designers are often left to rely on their own intuition and small
playtests to achieve this. Relying on subjective assessment makes it difficult for them to identify problem areas of
a game where a player's engagement depreciates - such as difficulty spikes or features that provoke inequity between
play styles.

This project integrates an indie game with a telemetry application, generating intuitive telemetry events for several
actions which are processed and presented in several ways. The objective is to aid the game designer's understanding of
a player's experience, and how they could maintain the game to maximise the overall player experience.
#### Project Backlog
#### Prioritised Requirements
#### Architecture Schema
#### Data Flow Diagram 
#### Telemetry Schema
#### Evaluation
#### Sprint Plan for sprint 2
### Meeting Notes
### Risk Register
### Ethical and Legal considerations
#### Privacy of Telemetry
#### Consent and Disclosure
#### Accessibility
#### IP and Licensing  
### License 
### Software and Data Inventory
### Deployment Guide
### Test Evidence
#### Automated Tests
#### End to End Tests
### Presentation
### Deployment and Operations Guide
#### Deployment Guide
#### Operations Guide
#### Maintenance and Troubleshooting Guide
#### Extension Guide
### Data Management Guide
#### Data Stored
#### Data Format 
## Java
### User Interface
#### Login
#### Main Menu
#### Settings
#### Start Run
#### Encounter
#### Shop
#### End Run
### Authentication and Access Control
#### Login
#### Password Reset
#### Player Permissions
#### Designer permissions
#### Developer Permissions
### Settings
#### Telemetry
#### Design Parameters
#### Simulation Mode
#### Role Assignment 
### Gameplay Loop
#### Start Run
#### Chose Difficulty
#### be in an Encounter
#### Use physical attack abilities
#### Use magical attack abilities
#### Kill enemies
#### Complete an encounter
#### View Shop
#### Purchase upgrades in shop
#### complete a run 
### Telemetry Events
Each telemetry event is sent
Telemetry events are validated
## Python
### User Interface
#### Login
#### Dashboard Views
#### Decision Log
#### Suggestions View
### Authentication and Access Control
#### Login
#### Password Reset
#### Player Permissions
#### Designer Permissions
### Developer Permissions
### Dashboard Views
#### Funnel View
#### Difficulty Spikes
#### Progress Curves
#### Fairness Indicators
#### Comparison Mode
### Telemetry Events
Each telemetry event is read
Telemetry events are validated
### Design Suggestions
### Seeded Dataset
#### 1,500 telemetry events
#### across 10 levels
#### 3 difficulty configurations
#### 30 balancing decision in log (good and band)
#### 150 anomilous telemetry events