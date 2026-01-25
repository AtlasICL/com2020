# Java Backend Test Suite Interface

## Explanation
Each subheading represents a single class within the program. Within each class, there can be unit tests - these test the behaviour of a method in isolation. There can also be integrated tests - these test the behaviour of a method when interacting with other elements of the program. The interface also contains several end-to-end tests - these test the behaviour of the program as a whole in appropriate scenarios.

## Unit and Integrated Tests

### GameManagerSingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### TimeManagerSingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### interface EntityAIInterface
#### Unit Tests
- testAbilityUsedIsValid()

- testUpgradePickedIsValidAndAffordable()

#### Integrated Tests
- testDefensiveAbilityImpactsAllies() *- Interacts with logic of AbilityInterface, EntityInterface*

- testOffensiveAbilityImpactsEnemies() *- Interacts with logic of AbilityInterface, EntityInterface*

### EntityAISingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### GameRunInterface
#### Unit Tests
- testCurrentStageToNextStage()

- testRunStartTimeIsValid()

- testDeathCountIsValid()

- testDeathCountIncrements()

- testEncounterPickedFromCorrectPhase()

- testCorrectNumberOfRandomUpgradesInShop()

- testExceptionThrownIfLackingResourcesForUpgrade()

#### Integrated Tests
- testUpgradeRemovedFromShopAndAppliedToPlayer() *- Interacts with logic of PlayerInterface*

### TelemetryListenerInterface
#### Unit Tests
- testSessionStartWrite()

- testNormalEncounterStartWrite()

- testNormalEncounterCompleteWrite()

- testNormalEncounterFailWrite()

- testNormalEncounterRetryWrite()

- testBossEncounterStartWrite() 

- testBossEncounterCompleteWrite()

- testBossEncounterFailWrite()

- testBossEncounterRetryEvent()

- testGainCoinWrite()

- testBuyUpgradeWrite()

- testEndSessionWrite()

- testSettingsChangeWrite()

- testKillEnemyWrite()

