## GameManager
*Acts as an interface between the front and back end. Is a singleton*
### Constructors
private GameManager()
### Fields
private static GameManager

private Settings

private TelemetryListener

private GameRun currentGame

private Encounter currentEncounter
### Methods
public static GameManager getGameManager()

%%To be designed by Luca C%%

## GameRun 
*Stores information relating to a single run of the game*
### Constructors
public GameRun(Difficulty difficulty) *creates a fresh game run*
### Fields
private Encounter[] phase1NormalEncounters *drawn from for stages 1 and 2*

private Encounter[] phase2NormalEncounters *drawn from for stages 4 and 5*

private Encounter[] phase3NormalEncounters *drawn from for stages 7 and 8*

private Encounter[] phase1BossEncounters *drawn from for stage 3*

private Encounter[] phase2BossEncounters *drawn from for stage 6*

private Encounter[] phase3BossEncounters *drawn from for stage 9*

private Encounter finalBoss *draw from for stage 10*

private UpgradeType[] shopUpgrades *drawn from to populate the shop*

private Player player

private int currentStage

private Difficulty currentDifficulty
### Methods
public Encounter pickEncounter() *returns a random encounter based on currentStage*

public UpgradeType[] viewShop() *returns 3 random upgrades in the shop*

public void purchaseUpgrade(UpgradeType) *buys and removes it from the shop and gives it to the player*

public Player getPlayer() 

## TelemetryListener
*Is notified of telemetry events, validates them, and writes them to the telemetry store. Use Jackson to write to the JSON file*
### Constructors
public TelemetryListener()
### Fields
private File JSONFile
### Methods
public void onSessionStart(SessionStartEvent e)

public void onNormalEncounterStart(NormalEncounterStartEvent e)

public void onNormalEncounterComplete(NormalEncounterCompleteEvent e)

public void onNormalEncounterFail(NormalEncounterFailEvent e)

public void onNormalEncounterRetry(NormalEncounterRetryEvent e)

public void onBossEncounterStart(BossEncounterStartEvent e)

public void onBossEncounterComplete(BossEncounterCompleteEvent e)

public void onBossEncounterFail(BossEncounterFailEvent e)

public void onBossEncounterRetry(BossEncounterRetryEvent e)

public void onGainCoin(GainCoinEvent e)

public void onBuyUpgrade(BuyUpgradeEvent e)

public void onEndSession(EndSessionEvent e)

public void onSettingsChange(SettingsChangeEvent e)

public void onKillEnemy(KillEnemyEvent e)

public void onStartTelemetry(StartTelemetryEvent e)
## abstract TelemetryEvent extends EventObject
*Contains fields all telemetry events contain*
### Constructors
public TelemetryEvent(int userID, int sessionID, String timestamp)
### Fields
private int userID

private int sessionID

private String timestamp
### Methods
public int getUserID()

public int getSessionID()

public String getTimestamp()
## SessionStartEvent extends TelemetryEvent
*Contains fields for sessions start*
### Constructors
public SessionStartEvent(int userID, int sessionID, String timestamp)
### Fields
### Methods

## NormalEncounterStartEvent extends TelemetryEvent
*Contains fields for normal encounter start*
### Constructors
public NormalEncounterStartEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)
### Fields
private EncounterType encounterName

private int stageNumber
### Methods
public String getEncounterName()

public int getStageNumber()
## NormalEncounterCompleteEvent extends TelemetryEvent
*Contains fields for normal encounter complete*
### Constructors
public NormalEncounterCompleteEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int playerHPRemaining)
### Fields
private EncounterType encounterName

private int stageNumber

private int playerHPRemaining
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

public int getPlayerHPRemaining()

## NormalEncounterFailEvent extends TelemetryEvent
*Contains fields for normal encounter fail*
### Constructors
public NormalEncounterFailEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)
### Fields
private EncounterType encounterName

private int stageNumber
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

## NormalEncounterRetryEvent extends TelemetryEvent
*Contains fields for normal encounter retry*
### Constructors
public NormalEncounterRetryEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int livesLeft)
### Fields
private EncounterType encounterName

private int stageNumber

private int livesLeft
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

public int getLivesLeft()
## BossEncounterStartEvent extends TelemetryEvent
*Contains fields for boss encounter start*
### Constructors
public BossEncounterStartEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)
### Fields
private EncounterType encounterName

private int stageNumber
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

## BossEncounterCompleteEvent extends TelemetryEvent
*Contains fields for boss encounter complete*
### Constructors
public BossEncounterCompleteEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int playerHPRemaining)
### Fields
private EncounterType encounterName

private int stageNumber

private int playerHPRemaining
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

public int getPlayerHPRemaining()
## BossEncounterFailEvent extends TelemetryEvent
*Contains fields for boss encounter fail*
### Constructors
public BossEncounterFailEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)
### Fields
private EncounterType encounterName

private int stageNumber
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

## BossEncounterRetryEvent extends TelemetryEvent
*Contains fields for boss encounter retry*
### Constructors
public BossEncounterRetryEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int livesLeft)
### Fields
private EncounterType encounterName

private int stageNumber

private int livesLeft
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

public int getLivesLeft()

## GainCoinEvent extends TelemetryEvent
*Contains fields for gain coin*
### Constructors
public GainCoinEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int coinsGained)
### Fields
private EncounterType encounterName

private int stageNumber

private int coinsGained

### Methods
public EncounterType getEncounterName()

public int getStageNumber()

public int getCoinsGained()

## BuyUpgradeEvent extends TelemetryEvent
*Contains fields for buy upgrade*
### Constructors
public BuyUpgradeEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, UpgradeType upgradeBought, int coinsSpent)
### Fields
private EncounterType encounterName

private int stageNumber

private int coinsSpent

private UpgradeType upgradeBought

### Methods
public EncounterType getEncounterName()

public int getStageNumber()

public int getCoinsSpent()

public UpgradeType getUpgradeBought()

## EndSessionEvent extends TelemetryEvent
*Contains fields for end session*
### Constructors
public EndSessionEvent(int userID, int sessionID, String timestamp)
### Fields
### Methods

## SettingsChangeEvent extends TelemetryEvent
*Contains fields for settings change*
### Constructors
public EndSessionEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, String setting, String settingValue)
### Fields
private EncounterTYpe encounterName

private String setting

private String settingValue
### Methods
public EncounterType getEncounterName()

public String getSetting()

public String getSettingValue()

## KillEnemyEvent extends TelemetryEvent
*Contains fields for kill enemy*
### Constructors
public EndSessionEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, String enemyType)
### Fields
private EncounterType encounterName

private int stageNumber

private String enemyType
### Methods
public EncounterTYpe getEncounterName()

public int getStageNumber()

public String getEnemyType()

## StartTelemetryEvent extends TelemetryEvent
*Contains fields for gain coin*
### Constructors
public EndSessionEvent(int userID, int sessionID, String timestamp)
### Fields
### Methods

## Settings
*Stores user settings as a singleton*
### Constructors
private Settings()
### Fields
private static Settings settings

private bool sendTelemetry

*integer values multiplied by these parameters are rounded*

private float hardEnemyMaxHealthMultiplier 

private float normalEnemyMaxHealthMultiplier

private float easyEnemyMaxHealthMultiplier

private float hardPlayerMaxHealthMultiplier *also affects upgrades that increase their max HP*

private float normalPlayerMaxHealthMultiplier

private float easyPlayerMaxHealthMultiplier

private float hardUpgradePriceMultiplier

private float normalUpgradePriceMultiplier

private float easyUpgradePriceMultiplier

private float hardEnemyDamageMultiplier

private float normalEnemyDamageMultiplier

private float easyEnemyDamageMultiplier

private int hardStartingLives

private int normalStartingLives

private int easyStartingLives

private float hardMagicCostMultiplier

private float normalMagicCostMultiplier

private float easyMagicCostMultiplier

private float hardMaxMagicMultiplier *also affects upgrades that increase their max Magic*

private float normalMaxMagicMultiplier

private float easyMaxMagicMultiplier

private float hardMagicRegenRate

private float normalMagicRegenRate

private float easyMagicRegenRate

### Methods
public bool getSendTelemetry()

public float getEnemyMaxHealthMultiplier(Difficulty difficulty)

public float getPlayerMaxHealthMultiplier(Difficulty difficulty)

public float getUpgradePriceMultiplier(Difficulty difficulty)

public float getEnemyDamageMultiplier(Difficulty difficulty)

public int getStartingLives(Difficulty difficulty)

public float getMagicCostMultiplier(Difficulty difficulty)

public float getMaxMagicMultiplier(Difficulty difficulty)

public float getMagicRegenRate(Difficulty difficulty)

## Encounter
*Stores all the enemies in an encounter, and information about said encounter*
### Constructors
### Fields
### Methods

## enum DamageType
*Stores all the type of damage*
### Constructors
### Fields
### Methods

## enum AbilityType
*Stores all the types of ability*
### Constructors
### Fields
### Methods

## enum UpgradeType
*Stores all the types of player upgrade*
### Constructors
### Fields
### Methods

## enum Difficulty
*Stores all the types levels of difficulty*
### Constructors
### Fields
### Methods

## enum EncounterType
*Stores all the types of encounter*
### Constructors
### Fields
### Methods

## interface Entity
*Contains all methods all entities have*
### Methods

## interface Player extends Entity
*Contains all methods all the player has*
### Methods

## ConcretePlayer implements Player
*Contains the concrete implementation of the player*
### Constructors
### Fields
### Methods

## abstract Enemy implements Entity
*Contains an implementation of health points that all enemies use*
### Constructors
### Fields
### Methods

## ConcreteEnemy extends Enemy
*Each enemy would have its own class, for example SkeletonEnemy, ZombieEnemy*
### Constructors
### Fields
### Methods

## interface Ability
*Interface all abilities implement*
### Methods

## ConcreteAbility implements Ability
*Different implementations for each ability*
### Constructors
### Fields
### Methods

## ConcreteUpgrade implements Player
*Decorates the player, giving their methods additional abilities and effects*
### Constructors
### Fields
### Methods