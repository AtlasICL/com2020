# Explanation
Each subheading represents a component of the game system. Unless explicitly stated the components type is a class and it's access specifier is public and it is in the top level of the package (unnested). The heading also states what it extends and implements. In the cases of extension and implementation all methods in the parent components are also in the child, but are not explicitly stated. If unspecified it extends object. Anything in italics is a comment or description of a component. 
# Interfaces
## GameManagerInterface 
*Interface for the game manager*
### Methods

%%To be designed by Luca C%%
## private GameManager implements GameManagerInterface nested in GameMangerSingleton
*Acts as an interface between the front and back end.*
### Constructors
public GameManager()
### Fields
private GameRunInterface currentGame

private EncounterInterface currentEncounter

## GameManagerSingleton
*Acts as a singleton holder for GameManager*
### Contractors
private GameManagerSingleton()
### Fields
private static GameManagerInterface gameManager
### Methods
public GameManagerInterface getGameManager()

## interface TimeManagerInterface
*Interface for the time manager*
### Methods
public LocalDateTime getCurrentTime()

## private TimeManager implements TimeManagerInterface nested in TimeManagerSingleton
*Time manager, that provide the time for all gameplay systems.*
### Constructors
public TimeManager()

## TimeManagerSingleton
*Acts as a singleton holder for TimeManager*
### Constructors
private TimeManagerSingleton
### Fields
private static TimeManagerInterface timeManager
### Methods
public TimeManagerInterface getTimeManager()

## interface EntityAIInterface
*Interface for entity AI*
### Methods
public void useAbility(Ability[] abilities, EntityInterface self, EntityInterface[] allies, EntityInterface[] enemies) 

public UpgradeType pickUpgrade(UpgradeType[] upgrades, int coins) 

## private RandomEntityAI implements EntityAIInterface nested in EntityAISingleton
*EntityAI that picks a random ability or upgrade when the relevant method is used. *
### Constructors
public EntityAIInterface()

## EntityAISingleton
*Provides singleton access to the entity AI system*
### Constructors
private EntityAISingleton()
### Fields
private static EntityAIInterface entityAI
### Methods
public EntityAIInterface getEntityAI()

## GameRunInterface
*Interface for a run of the game*
### Methods
public EncounterInterface pickEncounter() *returns a random encounter based on currentStage*

public UpgradeType[] viewShop() *returns 3 random upgrades in the shop*

public void purchaseUpgrade(UpgradeType upgrade) throws NotEnoughResourceException *buys and removes it from the shop and gives it to the player*

public PlayerInterface getPlayer() 

public void nextStage()

public int getStage()

public LocalDateTime getRunStartTime()

public int getDeathCount()

public void incrementDeathCount() *Only increments death count. A player running out of lives and the decrement of their lives should be handled by GameManger*

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

private EncounterInterface finalBoss *drawn from for stage 10*

private UpgradeType[] shopUpgrades *drawn from to populate the shop, removed from when an upgrade is bought*

private PlayerInterface player

private int currentStage

private Difficulty currentDifficulty
## TelemetryListenerInterface
*Interface for the telemetry listener*
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

## private TelemetryListener implements TelemetryListenerInterface nested in TelemetryListenerSingleton
*Is notified of telemetry events, validates them, and writes them to the telemetry store. Use Jackson to write to the JSON file*
### Constructors
public TelemetryListener()
### Fields
private File JSONFile

## TelemetryListenerSingleton
*Singleton access to the telemetry listener*
### Constructors
private TelemetryListenerSingleton()
### Fields
private static TelemetryListenerInterface telemetryListener
### Methods
public TelemetryListenerInterface getTelemetryListener()

## abstract TelemetryEvent extends EventObject
*Contains fields all telemetry events contain*
### Constructors
public TelemetryEvent(Object source, int userID, int sessionID, String timestamp)
### Fields
private final int userID

private final int sessionID

private final String timestamp
### Methods
public int getUserID()

public int getSessionID()

public String getTimestamp()
## abstract EncounterEvent extends TelemetryEvent
*Contains information relating to encounters*
### COnstructors
public EncounterEvent(Object source, int userId, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, Difficulty difficulty)
### Fields
private final int stageNumber

private final EncounterType encounterName

private final Difficulty difficulty
### Methods
public int getStageNumber()

public EncounterType getEncounterName()

public Difficulty getDifficulty()
## SessionStartEvent extends TelemetryEvent
*Contains fields for sessions start, sent when telemetry is enabled. When telemetry is enabled a new session ID is generated.*
### Constructors
public SessionStartEvent(Object source, int userID, int sessionID, String timestamp)
## NormalEncounterStartEvent extends EncounterEvent
*Contains fields for normal encounter start*
### Constructors
public NormalEncounterStartEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, Difficulty difficulty)

## NormalEncounterCompleteEvent extends EncounterEvent
*Contains fields for normal encounter complete*
### Constructors
public NormalEncounterCompleteEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int playerHPRemaining, Difficulty difficulty)
### Fields
private final int playerHPRemaining
### Methods
public int getPlayerHPRemaining()

## NormalEncounterFailEvent extends EncounterEvent
*Contains fields for normal encounter fail*
### Constructors
public NormalEncounterFailEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, Difficulty difficulty)

## NormalEncounterRetryEvent extends EncounterEvent
*Contains fields for normal encounter retry*
### Constructors
public NormalEncounterRetryEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int livesLeft, Difficulty difficulty)
### Fields
private final int livesLeft
### Methods
public int getLivesLeft()
## BossEncounterStartEvent extends EncounterEvent
*Contains fields for boss encounter start*
### Constructors
public BossEncounterStartEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, Difficulty difficulty)

## BossEncounterCompleteEvent extends EncounterEvent
*Contains fields for boss encounter complete*
### Constructors
public BossEncounterCompleteEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int playerHPRemaining, Difficulty difficulty)
### Fields
private final int playerHPRemaining
### Methods
public int getPlayerHPRemaining()
## BossEncounterFailEvent extends EncounterEvent
*Contains fields for boss encounter fail*
### Constructors
public BossEncounterFailEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, Difficulty difficulty)

## BossEncounterRetryEvent extends EncounterEvent
*Contains fields for boss encounter retry*
### Constructors
public BossEncounterRetryEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int livesLeft, Difficulty difficulty)
### Fields
private final int livesLeft
### Methods
public int getLivesLeft()

## GainCoinEvent extends EncounterEvent
*Contains fields for gain coin*
### Constructors
public GainCoinEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, int coinsGained, Difficulty difficulty)
### Fields
private final int coinsGained

### Methods
public int getCoinsGained()

## BuyUpgradeEvent extends EncounterEvent
*Contains fields for buy upgrade*
### Constructors
public BuyUpgradeEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, UpgradeType upgradeBought, int coinsSpent, Difficulty difficulty)
### Fields
private final int coinsSpent

private final UpgradeType upgradeBought

### Methods
public int getCoinsSpent()

public UpgradeType getUpgradeBought()

## EndSessionEvent extends TelemetryEvent
*Contains fields for end session, sent when telemetry is disabled.*
### Constructors
public EndSessionEvent(Object source, int userID, int sessionID, String timestamp)
## SettingsChangeEvent extends TelemetryEvent
*Contains fields for settings change*
### Constructors
public SettingsChangeEvent(iObject source, nt userID, int sessionID, String timestamp, String setting, String settingValue)
### Fields
private final String setting

private final String settingValue
### Methods
public String getSetting()

public String getSettingValue()

## KillEnemyEvent extends EncounterEvent
*Contains fields for kill enemy*
### Constructors
public KillEnemyEvent(Object source, int userID, int sessionID, String timestamp, EncounterType encounterName, int stageNumber, EntityType enemyType, Difficulty difficulty)
### Fields
private final EntityType enemyType
### Methods
public EntityType getEnemyType()

## SettingsInterface
*Interface for settings*
### Methods
public void createNewUser(String username, String password, Role role) throws AuthenticationException

public void authenticateUser(String username, String password) throws AuthenticationException

public Role getUserRole() throws AuthenticationException

public String getUsername() throws AuthenticationException

public int getSessionID() throws AuthenticationException

public int getUserID() throws AuthenticationException *performs a hash function on the username to generate their ID*

public bool isTelemetryEnabled() throws AuthenticationException

public int getMaxStageReached(Difficulty difficulty) throws AuthenticationException

public float getEnemyMaxHealthMultiplier(Difficulty difficulty) 

public float getPlayerMaxHealthMultiplier(Difficulty difficulty)

public float getUpgradePriceMultiplier(Difficulty difficulty)

public float getEnemyDamageMultiplier(Difficulty difficulty)

public int getStartingLives(Difficulty difficulty)

public float getMaxMagicMultiplier(Difficulty difficulty)

public int getMagicRegenRate(Difficulty difficulty)

public int getShopItemCount(Difficulty difficulty)

public void setTelemetryEnabled(bool telemetryEnabled)

public void setMaxStageReached(Difficulty difficulty, int maxStageReached)

public void setEnemyMaxHealthMultiplier(Difficulty difficulty, float enemyMaxHealthMultiplier)

public void setPlayerMaxHealthMultiplier(Difficulty difficulty, float playerMaxHealthMultiplier)

public void setUpgradePriceMultiplier(Difficulty difficulty, float upgradePriceMultiplier)

public void setEnemyDamageMultiplier(Difficulty difficulty, float enemyDamageMultiplier)

public void setStartingLives(Difficulty difficulty, int startingLives)

public void setMaxMagicMultiplier(Difficulty difficulty, float maxMagicMultiplier)

public void setMagicRegenRate(Difficulty difficulty, int magicRegenRate)

public void setShopItemCount(Difficulty difficulty, int shopItemCount)

## private Settings implements SettingsInterface nested in SettingsSingleton
*Stores user settings and statistics*
### Constructors
public Settings()
### Fields
private bool telemetryEnabled

private String username

private int sessionID *generated when the user logs in or when telemetry is enabled*

private Role userRole

private int hardMaxStageReached

private int normaMaxStageReached

private int easyMaxStageReached

*integer values multiplied by these parameters are rounded*

private float hardEnemyMaxHealthMultiplier 

private float normalEnemyMaxHealthMultiplier

private float easyEnemyMaxHealthMultiplier

private float hardPlayerMaxHealthMultiplier

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

private float hardMaxMagicMultiplier

private float normalMaxMagicMultiplier

private float easyMaxMagicMultiplier

private int hardMagicRegenRate

private int normalMagicRegenRate

private int easyMagicRegenRate

private int hardShopItemCount

private int normalShopItemCount

private int easyShopItemCount
## SettingsSingleton
*Provides singleton access to the user's settings*
### Constructors
private SettingsSingleton()
### Fields
private static SettingsInterface settings
### Methods
pubic SettingsInterface getSettings()

## AuthenticationException extends Exception
*Exception for failed authentication*
### Constructors
public AuthenticationException()

public AuthenticationException(String message)

public AuthenticationException(String message, Throwable cause)

## enum Role
*Enumerates all user roles*
### Fields
PLAYER, DESIGNER, DEVELOPER

## EncounterInterface
*Interface for encounters*
### Methods
public EntityInterface[] getEnemies()

public bool isComplete()

public void markComplete()

public EncounterType getType()

public void resetEnemyHealth()

## Encounter implements EncounterInterface
*Stores all the enemies in an encounter, and information about said encounter*
### Constructors
public Encounter(EncounterType type)
### Fields
private EntityInterface[] enemies 

private bool completed

private EncounterType encounterType

## enum DamageType
*Enumerates all the type of damage*
### Fields
PHYSICAL, FIRE, WATER, THUNDER, ABSOLUTE

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

public PlayerInterface applyUpgrade(PlayerInterface player) *done using reflection*

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
public EntityInterface createEnemy() *Done using reflection*

## interface EntityInterface
*Contains all methods all entities have*
### Methods
public void loseHealth(int amount, DamageType type) throws IllegalArgumentException *amount cannot be negative*

public void gainHealth(int amount) throws IllegalArgumentException *amount cannot be negative, can have health exceed maxHealth*

public int getHealth()

public int getMaxHealth()

public int calcDamage(int base, DamageType type) *applies modifiers to damage type and returns the result*

public List\<AbilityInterface> getAbilities()

public void resetHealth()

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
public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException *throws error when the creature attempting to use it doesn't have enough resource to do so*

public bool isOffensive()

public String getDescription()

public int getNumberOfTargets()

public AbilityType getType()

## LackingResourceException extends Exception
*Exception for when an ability is attempted without sufficient resources*
### Constructors
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