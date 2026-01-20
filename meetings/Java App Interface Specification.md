# Explanation
Each subheading is the specified type (e.g. interface or enum). If unspecified it is a class. Anything in italics is a comment or description about a component. If a heading (e.g. methods) is not present for an interface, it means there is nothing there (e.g. it has no methods). If an interface extends or implements another, all inherited methods are present but are not explicitly stated. 
# Interfaces
## GameManager
*Acts as an interface between the front and back end. Is a singleton*
### Constructors
private GameManager()
### Fields
private static GameManager

private Settings settings

private TelemetryListener telemetryListener

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

private UpgradeType[] shopUpgrades *drawn from to populate the shop, removed from when an upgrade is bought*

private Player player

private int currentStage

private Difficulty currentDifficulty
### Methods
public Encounter pickEncounter() *returns a random encounter based on currentStage*

public UpgradeType[] viewShop() *returns 3 random upgrades in the shop*

public void purchaseUpgrade(UpgradeType) *buys and removes it from the shop and gives it to the player*

public Player getPlayer() 

public void nextStage()

public int getStage()

## TelemetryListener
*Is notified of telemetry events, validates them, and writes them to the telemetry store. Use Jackson to write to the JSON file. Is a singleton*
### Constructors
public TelemetryListener()
### Fields
private File JSONFile
### Methods
public TelemetryListener getTelemetryListener()

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
## SettingsChangeEvent extends TelemetryEvent
*Contains fields for settings change*
### Constructors
public SettingsChangeEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, String setting, String settingValue)
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
public KillEnemyEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, EntityType enemyType)
### Fields
private EncounterType encounterName

private int stageNumber

private EntityType enemyType
### Methods
public EncounterType getEncounterName()

public int getStageNumber()

public EntityType getEnemyType()

## StartTelemetryEvent extends TelemetryEvent
*Contains fields for starting sending telemetry*
### Constructors
public StartTelemetryEvent(int userID, int sessionID, String timestamp)

## Settings
*Stores user settings as a singleton*
### Constructors
private Settings()
### Fields
private static Settings settings

private bool telemetryEnabled

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
public bool isTelemetryEnabled()

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
public Encounter(EncounterType type)
### Fields
private Entity[] enemies 

private bool completed

private EncounterType encounterType
### Methods
public Entity[] getEnemies()

public bool isComplete()

public void markComplete()

pubic EncounterType getType()

## enum DamageType
*Enumerates all the type of damage*
### Fields
PHYSICAL, FIRE, COLD, ACID, POISON, SONIC, ELECTRIC, RADIANT, NECROTIC, PSYCHIC, ABSOLUTE

## enum AbilityType
*Enumerates the game's abilities*
### Constructors
private AbilityType(Constructor abilityConstructor)
### Fields
BOW, SWORD, etc. *placeholders, abilities TBD*

private final Constructor abilityConstructor
### Methods
public Ability getAbility()

## enum UpgradeType
*Enumerates the game's upgrades*
### Constructor
private UpgradeType(int price, Constructor upgradeConstructor)
### Fields
HEALTH_BOOST, POTENT_MAGIC, etc. *placeholders, upgrades TBD*

private final int price

private final Constructor upgradeConstructor
### Methods
public int getPrice()

public Player applyUpgrade(Player player) *done using reflection*

## enum Difficulty
*Enumerates the different levels of difficulty*
### Fields
EASY, MEDIUM, HARD

## enum EncounterType
*Enumerates the games encounters*
### Constructors
private EncounterType(EntityType[] enemies)
### Fields
ZOMBIE_HORDE, SKELETON_RANGERS, etc. *placeholder, encounters TBD*
### Methods
public EntityType[] getEnemies()

public Encounter createEncounter() *done using reflection via EntityType*

## enum EntityType
*Enumerates the games entities*
### Constructor
private EntityType(Constructor entityConstructor)
### Fields
ZOMBIE, SKELETON, etc. *placeholder, entities TBD*

private final Constructor entityConstructor
### Methods
public Entity createEnemy() *Done using reflection*

## interface Entity
*Contains all methods all entities have*
### Methods
public void loseHealth(int amount, DamageType type) throws IllegalArgumentException *amount cannot be negative*

public void gainHealth(int amount) throws IllegalArgumentException *amount cannot be negative, can have health exceed maxHealth*

public int getHealth()

public int getMaxHealth()

public int calcDamage(int base, DamageType type) *applies modifiers to damage type and returns the result*

public List\<Ability> getAbilities()

public EntityType getType()
## interface Player extends Entity
*Contains all methods all the player has*
### Methods
public int getCoins()

public void loseCoins(int amount) throws IllegalArgumentException *amount cannot be negative*

public void gainCoins(int amount) throws IllegalArgumentException *amount cannot be negative*

public int getMagic()

public int getMaxMagic()

public int getMagicRegenRate()

public void gainMagic(int amount) throws IllegalArgumentException *amount cannot be negative, can exceed maximum*

public void loseMagic(int amount) throws IllegalArgumentException *amount cannot be negative*

public int getLives()

public void gainLives(int amount) throws IllegalArgumentException *amount cannot be negative*

public void loseLives(int amount) throws IllegalArgumentException *amount cannot be negative*

List\<UpgradeType> getUpgrades()
## ConcretePlayer implements Player
*Contains the concrete implementation of the player*
### Constructors
public Player() *reads values from settings, and adjusting it's statistics using them (e.g. health)*
### Fields
private int maxMagic

private int maxHealth

private int health

private int magic

private int coins

private int lives

private List\<Ability> abilities

## abstract Enemy implements Entity
*Contains an implementation of health points that all enemies use*
### Constructors
public Enemy() *reads values from settings, and adjusting it's statistics using them (e.g. health)*
### Fields
private int health

private int maxHealth

## ConcreteEnemy extends Enemy
*Each enemy would have its own class, for example SkeletonEnemy, ZombieEnemy*
### Constructors
public ConcreteEnemy() *reads values from settings, and adjusting it's statistics using them (e.g. health)*

## interface Ability
*Interface all abilities implement*
### Methods
public void execute(Entity source, Entity[] targets)

public bool isOffensive()

public String getDescription()

public int getNumberOfTargets()

public AbilityType getType()

## ConcreteAbility implements Ability
*Different implementations for each ability*
### Constructors
public Ability()
### Fields
private final static bool offensive

private final static String description

private final static int numberOfTargets

private final static AbilityType type

## ConcreteUpgrade implements Player
*Decorates the player, giving their methods additional abilities and effects*
### Constructors
public ConcreteUpgrade(Player player)
### Fields
private Player player