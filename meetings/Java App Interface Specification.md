# Explanation
Each subheading is the specified type (e.g. interface or enum). If unspecified it is a class. Anything in italics is a comment or description about a component. If a heading (e.g. methods) is not present for an interface, it means there is nothing there (e.g. it has no methods). If an interface extends or implements another, all inherited methods are present but are not explicitly stated. 
# Interfaces
## GameManagerInterface
*Interface for the game manager*
### Methods
public static GameManager getGameManager()

%%To be designed by Luca C%%
## GameManager implements GameManagerInterface
*Acts as an interface between the front and back end. Is a singleton*
### Constructors
private GameManager()
### Fields
private static GameManagerInterface gameManager

private GameRun currentGame

private Encounter currentEncounter

## GameRunInterface
*Interface for a run of the game*
### Methods
public EncounterInterface pickEncounter() *returns a random encounter based on currentStage*

public UpgradeType[] viewShop() *returns 3 random upgrades in the shop*

public void purchaseUpgrade(UpgradeType) *buys and removes it from the shop and gives it to the player*

public PlayerInterface getPlayer() 

public void nextStage()

public int getStage()


## GameRun implements GameRunInterface
*Stores information relating to a single run of the game*
### Constructors
public GameRun(Difficulty difficulty) *creates a fresh game run*
### Fields
private EncounterInterface[] phase1NormalEncounters *drawn from for stages 1 and 2*

private EncounterInterface[] phase2NormalEncounters *drawn from for stages 4 and 5*

private EncounterInterface[] phase3NormalEncounters *drawn from for stages 7 and 8*

private EncounterInterface[] phase1BossEncounters *drawn from for stage 3*

private EncounterInterface[] phase2BossEncounters *drawn from for stage 6*

private EncounterInterface[] phase3BossEncounters *drawn from for stage 9*

private EncounterInterface finalBoss *draw from for stage 10*

private UpgradeType[] shopUpgrades *drawn from to populate the shop, removed from when an upgrade is bought*

private PlayerInterface player

private int currentStage

private Difficulty currentDifficulty
## TelemetryListenerInterface
*Interface for the telemetry listener*
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

## TelemetryListener implements TelemetryListenerInterface
*Is notified of telemetry events, validates them, and writes them to the telemetry store. Use Jackson to write to the JSON file. Is a singleton*
### Constructors
public TelemetryListener()
### Fields
private File JSONFile


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
## abstract EncounterEvent extends TelemetryEvent
*Contains information relating to encounters*
### COnstructors
public EncounterEvent(int userId, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)
### Fields
private int stageNumber

private EncounterType encounterName
### Methods
public int getStageNumber()

public EncounterType getEncounterName()
## SessionStartEvent extends TelemetryEvent
*Contains fields for sessions start*
### Constructors
public SessionStartEvent(int userID, int sessionID, String timestamp)
## NormalEncounterStartEvent extends EncounterEvent
*Contains fields for normal encounter start*
### Constructors
public NormalEncounterStartEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)

## NormalEncounterCompleteEvent extends EncounterEvent
*Contains fields for normal encounter complete*
### Constructors
public NormalEncounterCompleteEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int playerHPRemaining)
### Fields
private int playerHPRemaining
### Methods
public int getPlayerHPRemaining()

## NormalEncounterFailEvent extends EncounterEvent
*Contains fields for normal encounter fail*
### Constructors
public NormalEncounterFailEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)

## NormalEncounterRetryEvent extends EncounterEvent
*Contains fields for normal encounter retry*
### Constructors
public NormalEncounterRetryEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int livesLeft)
### Fields
private int livesLeft
### Methods
public int getLivesLeft()
## BossEncounterStartEvent extends EncounterEvent
*Contains fields for boss encounter start*
### Constructors
public BossEncounterStartEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)

## BossEncounterCompleteEvent extends EncounterEvent
*Contains fields for boss encounter complete*
### Constructors
public BossEncounterCompleteEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int playerHPRemaining)
### Fields
private int playerHPRemaining
### Methods
public int getPlayerHPRemaining()
## BossEncounterFailEvent extends EncounterEvent
*Contains fields for boss encounter fail*
### Constructors
public BossEncounterFailEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber)

## BossEncounterRetryEvent extends EncounterEvent
*Contains fields for boss encounter retry*
### Constructors
public BossEncounterRetryEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int livesLeft)
### Fields
private int livesLeft
### Methods
public int getLivesLeft()

## GainCoinEvent extends EncounterEvent
*Contains fields for gain coin*
### Constructors
public GainCoinEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int coinsGained)
### Fields
private int coinsGained

### Methods
public int getCoinsGained()

## BuyUpgradeEvent extends EncounterEvent
*Contains fields for buy upgrade*
### Constructors
public BuyUpgradeEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, UpgradeType upgradeBought, int coinsSpent)
### Fields
private int coinsSpent

private UpgradeType upgradeBought

### Methods
public int getCoinsSpent()

public UpgradeType getUpgradeBought()

## EndSessionEvent extends TelemetryEvent
*Contains fields for end session*
### Constructors
public EndSessionEvent(int userID, int sessionID, String timestamp)
## SettingsChangeEvent extends TelemetryEvent
*Contains fields for settings change*
### Constructors
public SettingsChangeEvent(int userID, int sessionID, String timestamp, String setting, String settingValue)
### Fields
private String setting

private String settingValue
### Methods
public String getSetting()

public String getSettingValue()

## KillEnemyEvent extends EncounterEvent
*Contains fields for kill enemy*
### Constructors
public KillEnemyEvent(int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, EntityType enemyType)
### Fields
private EntityType enemyType
### Methods
public EntityType getEnemyType()
## SettingsInterface
*Interface for settings*
### Methods
public bool isTelemetryEnabled()

public float getEnemyMaxHealthMultiplier(Difficulty difficulty)

public float getPlayerMaxHealthMultiplier(Difficulty difficulty)

public float getUpgradePriceMultiplier(Difficulty difficulty)

public float getEnemyDamageMultiplier(Difficulty difficulty)

public int getStartingLives(Difficulty difficulty)

public float getMagicCostMultiplier(Difficulty difficulty)

public float getMaxMagicMultiplier(Difficulty difficulty)

public int getMagicRegenRate(Difficulty difficulty)

public int getShopItemCount(Difficulty difficulty)
## Settings implements SettingsInterface
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

private int hardMagicRegenRate

private int normalMagicRegenRate

private int easyMagicRegenRate

private int hardShopItemCount

private int normalShopItemCount

private int easyShopItemCount
## EncounterInterface
*Interface for encounters*
### Methods
public Entity[] getEnemies()

public bool isComplete()

public void markComplete()

public EncounterType getType()

## Encounter implements EncounterInterface
*Stores all the enemies in an encounter, and information about said encounter*
### Constructors
public Encounter(EncounterType type)
### Fields
private Entity[] enemies 

private bool completed

private EncounterType encounterType

## enum DamageType
*Enumerates all the type of damage*
### Fields
PHYSICAL, FIRE, COLD, TOXIC, SONIC, ELECTRIC, RADIANT, NECROTIC, PSYCHIC, ABSOLUTE

## enum AbilityType
*Enumerates the game's abilities*
### Constructors
private AbilityType(Constructor abilityConstructor)
### Fields
BOW, SWORD, etc. *placeholders, abilities TBD*

private final Constructor abilityConstructor
### Methods
public AbilityInterface getAbility()

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

## interface EntityInterface
*Contains all methods all entities have*
### Methods
public void loseHealth(int amount, DamageType type) throws IllegalArgumentException *amount cannot be negative*

public void gainHealth(int amount) throws IllegalArgumentException *amount cannot be negative, can have health exceed maxHealth*

public int getHealth()

public int getMaxHealth()

public int calcDamage(int base, DamageType type) *applies modifiers to damage type and returns the result*

public List\<AbilityInterface> getAbilities()

public EntityType getType()
## interface PlayerInterface extends EntityInterface
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
## Player implements PlayerInterface
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

private List\<AbilityInterface> abilities

## abstract Enemy implements EntityInterface
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

## interface AbilityInterface
*Interface all abilities implement*
### Methods
public void execute(Entity source, Entity[] targets) throws LackingResourceException *throws error when the creature attempting to use it doesn't have enough resource to do so*

public bool isOffensive()

public String getDescription()

public int getNumberOfTargets()

public AbilityType getType()

## LackingResourceException extends Exception
*Exception for when an ability is attempted without sufficient resources*
### Constructor
public LackingResourceException()

public LackingResourceException(String message)

public LackingResourceException(String message, Throwable cause)

## ConcreteAbility implements AbilityInterface
*Different implementations for each ability*
### Constructors
public ConcreteAbility()
### Fields
private final static bool offensive

private final static String description

private final static int numberOfTargets

private final static AbilityType type

## ConcreteUpgrade implements PlayerInterface
*Decorates the player, giving their methods additional abilities and effects*
### Constructors
public ConcreteUpgrade(PlayerInterface player)
### Fields
private PlayerInterface player