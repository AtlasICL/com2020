# Java Backend Test Suite Interface

## Explanation
Each subheading represents a single class within the program. Within each class, there can be unit tests - these test the behaviour of a class in isolation. There can also be integration tests - these test the behaviour of a class when interacting with the logic of other classes in the program. The interface also contains several end-to-end tests - these test the behaviour of the program as a whole in appropriate scenarios.

## Approach
JUnit (v5.10.1) will be used as the framework for this test suite. Java Reflection will be used to create and assess mock objects at runtime, and to facilitate the testing of private methods and attributes.

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

### RandomEntityAI
#### Unit Tests
- testUpgradeOnlyPickedIfAffordable()

- testPurchasedUpgradeAppliedToSelf()

#### Integration Tests
- testAbilityImpactsEnemies() *- Interacts with logic of AbilityInterface, EntityInterface*

### EntityAISingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### GameRun
#### Unit Tests
- testCurrentStageIncrementsToNextStage()

- testRunStartTimeIsValid()

- testDeathCountIsValid()

- testDeathCountIncrementation()

- testEncounterPickedFromCorrectPhase()

- testCorrectNumberOfUpgradesInShop()

- testUpgradesInShopAreRandomised()

- testUpgradeRemovedFromShopWhenPurchased()

- testExceptionThrownIfLackingResourcesForUpgrade()

#### Integration Tests
- testUpgradeAppliedToPlayer() *- Interacts with logic of UpgradeType*

- testLifeLostIfDeathCountIncrements() *- Interacts with logic of PlayerInterface*

### TelemetryListener
#### Unit Tests
- testUserIDValidation()

- testSessionIDValidation()

- testTimeStampValidation()

- testTelemetryNameValidation()

- testEncounterNameValidation()

- testStageNumberValidation()

- testPlayerHPRemainingValidation()

- testLivesLeftValidation()

- testCoinsGainedValidation()

- testUpgradeBoughtValidation()

- testCoinsSpentValidation()

- testSettingsValidation()

- testSettingValueValidation()

- testEnemyTypeValidation()

- testDifficultyValidation()

#### Integration Tests
- testTelemetryWrittenIfUserOptedInToTelemetry() *- Interacts with I/O in JSON file*

- testTelemetryNotWrittenIfUserOptedOutOfTelemetry() *- Interacts with I/O in JSON file*

### TelemetryListenerSingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### Settings
#### Unit Tests
- testExceptionThrownIfUsernameInvalidWhenCreatingUser()
  
- testExceptionThrownIfPasswordInvalidWhenCreatingUser()

- testExceptionThrownIfUsernameOrPasswordInvalidWhenAuthenticating()
  
- testUserIDHashFunctionIsConsistent()
  
- testUniqueSessionIDCreated()
  
- testExceptionThrownIfGetOrSetMethodInvokerIsNotDeveloper()

- testDeveloperCanInvokeGetOrSetMethods()

- testExceptionThrownIfUsernameInvalidWhenSettingRole()

- testFirstCreatedAccountIsDeveloper()

- testAllOtherCreatedAccountsArePlayers()

### SettingsSingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### Encounter
#### Unit Tests
- testEnemiesExist()

- testCompletionStatusChangesWhenMarkedComplete()

- testEnemyHPIsReset()

### enum AbilityType
#### Integration Tests
- testExecutedAbilityImpactsTarget() *- Interacts with logic of EntityInterface

### enum UpgradeType
#### Integration Tests
- testUpgradeAppliedToPlayer() *- Interacts with logic of PlayerInterface*

### enum EncounterType
#### Unit Tests
- testValidEncounterIsCreated()

### enum EntityType
#### Unit Tests
- testValidEnemyIsCreated()

### Entity (Player/Enemy)
#### Unit Tests
- testHealthLossEqualToDamageCalculation()

- testExceptionThrownIfHealthLossAmountIsNegative()

- testHealthResetRestoresMaxHealth()

### Player
#### Unit Tests
- testLoseCoins()

- testGainCoins()

- testLoseMagic()

- testGainMagic()

- testLoseLives()

- testGainLives()

- testExceptionThrownIfGainOrLossAmountIsNegative()


## End-To-End Tests

- testPlayerWinsNormalEncounter()

- testPlayerLosesNormalEncounterWithLivesLeft()
  
- testPlayerLosesNormalEncounterWithoutLivesLeft()
  
- testPlayerWinsBossEncounter()
  
- testPlayerLosesBossEncounterWithLivesLeft()
  
- testPlayerLosesBossEncounterWithoutLivesLeft()
  
- testPlayerWinsFinalEncounter()
