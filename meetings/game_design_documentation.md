# Game Design Documentation

This document serves as a non-technical overview of the game's design.

## Level Structure
The game will exist in two parts: combat encounters and shops. There are 10 combat encounters in the game, and a shop will follow each one, excluding the final combat encounter.

## Combat Encounter Design
When in a combat encounter, the game functions as turn-based. The player will always move first. The player will be able to see a simple representation of their character and their undefeated opponent(s), as well as a dialogue box/interface. This box has two purposes: to narrate the scene ("You encounter a Fish Man!", "You were hit for 20 damage point(s)", "You won the battle", etc.), and to serve as the player's interface, allowing the player to select from their available attack abilities (with information such as magic cost and damage type). Once an attack is selected, the player is prompted to select which opponent they wish to damage. Following this, the opponent's turn takes place, where, like the player, an opponent will select from one of their abilities to use on the player.

## Damage Types
Both the player and the enemies that they can encounter are affected differently by different damage types. For example, if the player were to cast a water-based spell at a "pyromancer" enemy, the attack would be more effective than if it were cast on a "Fish Man" enemy.

Each attack ability in the game has a damage type. The damage types are as follows: Physical, Fire, Water, Thunder, Absolute.

Attacks of the Absolute type are always effective on any opponent.

## Attack Abilities
When the game begins, the player character only has one attacking ability – that being "Punch", a low-damage physical move.

Attacks that are not physical all cost magic points. Different attack abilities use different amounts of magic points.

Within the shop screens in between encounters, players can exchange their coins earned in battle for attack abilities. They are as follows:

| Name | Shop Cost (coins) | Magic Points | Damage Type | Notes |
|------|-------------------|--------------|-------------|-------|
| Absolute Pulse | 10 | 20 | Absolute | |
| Slash | 15 | 0 | Physical | Slash > Punch |
| Water Jet | 20 | 20 | Water | |
| Thunder Storm | 25 | 30 | Thunder | Hits all opponents |
| Fire Ball | 30 | 40 | Fire | High damage |

## Passive Abilities
When the game begins, the player character will have no non-attack abilities. These can also be purchased in the shop in between encounters. The purpose of these abilities is to allow the player to become stronger in non-attacking ways.

The names of these abilities are descriptive and simple.

| Name | Shop Cost (coins) |
|------|-------------------|
| Physical Damage Resistance | 20 |
| Fire Damage Resistance | 10 |
| Water Damage Resistance | 5 |
| Thunder Damage Resistance | 5 |
| Improved Physical Damage | 20 |
| Improved Fire Damage | 15 |
| Improved Water Damage | 10 |
| Improved Thunder Damage | 10 |

## Structure of Levels
The game is split into three distinct phases:

**Phase 1**, where players will be given simple encounters to teach them the basic rules of the game. This phase ends with a boss at level 3. Players will only fight one enemy at a time in Phase 1. Each encounter rewards the player with 10 coins.

**Phase 2**, where players must deal with a steady climb in challenge. This phase ends with a boss at level 7. Players can encounter two enemies at once in Phase 2. Each encounter rewards the player with 15 coins.

**Phase 3**, where players will have their skills put to the test in highly difficult encounters. This phase ends with a boss at level 9 and a final boss at level 10. Players can encounter three enemies at a time, in level 8 in particular. Each encounter rewards the player with 20 coins.

## Enemies Within Encounters
There are many enemies that the player will encounter within the game. The range of enemies that can be encountered increases as the phases increase.

### Phase 1 Enemies
#### Goblin
- Health: Medium
- Actions: Slash
- Resistant to: None
- Vulnerable to: None

#### Fish Man
- Health: Medium
- Actions: Punch, Water Jet
- Resistant to: Water
- Vulnerable to: Thunder

#### Pyromancer
- Health: Low
- Actions: Fire Ball
- Resistant to: Fire
- Vulnerable to: Water

#### Evil Wizard (Boss)
- Health: Medium
- Actions: Thunder Storm, Fire Ball
- Resistant to: Immune to anything not Physical/Absolute
- Vulnerable to: Physical, Absolute

### Phase 2 Enemies
(All non-boss enemies that have appeared in Phase 1)

#### Armoured Goblin
- Health: High
- Actions: Slash
- Resistant to: Mundane, Fire, Water
- Vulnerable to: Thunder

#### Ghost (Boss)
- Health: High
- Actions: Thunder Storm, Absolute Pulse, Fire Ball
- Resistant to: Physical (fully immune)
- Vulnerable to: Absolute

### Phase 3 Enemies
(All non-boss enemies that have appeared in Phase 2)

#### Black Knight (Boss)
- Health: High
- Actions: Slash
- Resistant to: Physical
- Vulnerable to: Thunder

#### Dragon (Final Boss)
- Health: Very High
- Actions: Slash, Fire Ball, Thunder Storm
- Resistant to: Fire, Thunder
- Vulnerable to: Absolute

## Shop Design
The shop exists as a single screen. Four random abilities are presented to the user, alongside their price, and whether they are an "Attack" or "Passive". Providing the player can afford the abilities, they will be able to buy as many of the four as they please. Once the player decides they are done, they can select "Next Encounter" and continue the game.

