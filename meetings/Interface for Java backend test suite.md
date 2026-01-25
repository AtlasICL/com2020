# Java Backend Test Suite Interface

## Explanation
Each subheading represents a single class within the program. Within each class, there can be unit tests - these test the behaviour of a class in isolation. There can also be integrated tests - these test the behaviour of a class when interacting with the logic of other classes in the program. The interface also contains several end-to-end tests - these test the behaviour of the program as a whole in appropriate scenarios.

## Approach
JUnit will be used as the framework for this test suite. Java Reflection will be used to create and assess mock objects at runtime, and to facilitate the testing of private methods and attributes.

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
- testUpgradeRemovedFromShopAndAppliedToPlayer() *- Interacts with logic of UpgradeType*

- testLifeLostIfDeathCountIncrements() *- Interacts with logic of PlayerInterface*

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

### TelemetryListenerSingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### abstract TelemetryEvent extends EventObject
#### Unit Tests
- testUserIDValidation()
  
- testSessionIDValidation()

- testTimestampValidation()

### abstract EncounterEvent extends TelemetryEvent
#### Unit Tests
- testEncounterNameValidation()
  
- testStageNumberValidation()

### EncounterCompleteEvent extends EncounterEvent
#### Unit Tests
- testPlayerHPRemainingValidation()

### EncounterRetryEvent extends EncounterEvent
#### Unit Tests
- testLivesLeftValidation()

### GainCoinEvent extends EncounterEvent
#### Unit Tests
- testCoinsGainedValidation()

### BuyUpgradeEvent extends EncounterEvent
#### Unit Tests
- testUpgradeBoughtValidation()

- testCoinsSpentValidation()

### SettingsChangeEvent extends TelemetryEvent
#### Unit Tests
- testSettingsValidation()

- testSettingValueValidation()

### KillEnemyEvent extends EncounterEvent
#### Unit Tests
- testEnemyTypeValidation()

- testDifficultyValidation()

### SettingsInterface
#### Unit Tests
- testUsernameCannotBeNull()
  
-	testPasswordCannotBeNull()
  
-	testUserIDIsValid()
  
-	testUniqueSessionIDCreated()
  
-	testUserRoleExists()

-	testFirstCreatedAccountIsDeveloper()

-	testAllOtherCreatedAccountsArePlayers()
  
-	testEnabledAndDisabledTelemetry()
  
-	testEnemyMaxHealthMultiplier()
  
-	testPlayerMaxHealthMultiplier()
  
-	testUpgradePriceMultipler()
  
-	testEnemyDamageMultiplier()

-	testStartingLives()
  
-	testMaxMagicMultiplier()
  
-	testMagicRegenRate()
  
-	testShopItemCount()

### SettingsSingleton
#### Unit Tests
*We need to verify that this class meets the core properties of the Singleton design pattern.*

- testSingleInstance()

- testConstructorIsPrivate()

### EncounterInterface
#### Unit Tests
- testEnemiesExist()

- testStageIncompleteToComplete()

- testEncounterTypeIsValid()

- testEnemyHPReset()

### enum AbilityType
#### Unit Tests
- testGetAssociatedAbility()

### enum UpgradeType
#### Integrated Tests
- testUpgradeAppliedToPlayer() *- Interacts with logic of PlayerInterface*

### enum EncounterType
#### Unit Tests
- testValidEncounterCreated()

### enum EntityType
#### Unit Tests
- testValidEnemyCreated()

### interface EntityInterface
#### Unit Tests
- testHealthLossEqualToDamageCalculation()

- testHealthGain()

- testHealthResetRestoresMaxHealth()

### interface PlayerInterface extends EntityInterface
#### Unit Tests
- testCoinsLoss()

- testCoinsGain()

- testMagicLoss()

- testMagicGain()

- testLivesLoss()

- testLivesGain()

### interface AbilityInterface
#### Unit Tests
- testNumberOfTargetsMatchesExecutionTargets()

#### Integrated Tests
- testSourceExecuteDefensiveAbility() *- Interacts with logic of PlayerInterface, Enemy*

- testSourceExecuteOffensiveAbility() *- Interacts with logic of PlayerInterface, Enemy*

## End-To-End Tests

- testPlayerWinsNormalEncounter()

- testPlayerLosesNormalEncounterWithLivesLeft()
  
- testPlayerLosesNormalEncounterWithoutLivesLeft()
  
- testPlayerWinsBossEncounter()
  
- testPlayerLosesBossEncounterWithLivesLeft()
  
- testPlayerLosesBossEncounterWithoutLivesLeft()
  
- testPlayerWinsFinalEncounter()
