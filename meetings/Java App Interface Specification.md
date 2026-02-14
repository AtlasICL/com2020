# Explanation
Each subheading represents a component of the game system. Unless explicitly stated the components type is a class and it's access specifier is public and it is in the top level of the package (unnested). The heading also states what it extends and implements. In the cases of extension and implementation all methods in the parent components are also in the child, but are not explicitly stated. If unspecified it extends object. Anything in italics is a comment or description of a component. 
# Purpose
This specification was created early on and served as a framework while the group worked on each component. Due to the rapid development of the system this interface has become outdated, but this does not matter as it has served its purpose and is not meant to be a serious piece of documentation. See the Report and Javadoc for formal documentation of the application.
# Interfaces
## GameManagerInterface 
*Interface for the game manager*
### Methods
public boolean isGameRunning()

public DifficultyEnum getCurrentDifficulty()

public void startNewGame(DifficultyEnum difficulty)

public GameRunInterface getCurrentRun()

public PlayerInterface getCurrentPlayer()

public EncounterInterface pickEncounter()

public EncounterInterface getCurrentEncounter()

public void resetFailedEncounter()

public void completeCurrentEncounter()

public void advanceToNextLevel()

public UpgradeEnum[] viewShop()

public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException

public void endGame()

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
public GameManagerInterface getInstance()

## interface TimeManagerInterface
*Interface for the time manager*
### Methods
public LocalDateTime getCurrentTime()

public static String convertDateTime(LocalDateTime time)
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
public TimeManagerInterface getInstance()

## interface EntityAIInterface
*Interface for entity AI*
### Methods
public void useAbility(AbilityEnum[] abilities, EntityInterface self, EntityInterface[] enemies) 

public UpgradeEnum pickUpgrade(UpgradeEnum[] upgrades, int coins) 

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
public EntityAIInterface getInstance()

## GameRunInterface
*Interface for a run of the game*
### Methods
public EncounterInterface pickEncounter() *returns a random encounter based on currentStage*

public UpgradeEnum[] viewShop() *returns N random upgrades in the shop*

public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException *buys and removes it from the shop and gives it to the player*

public PlayerInterface getPlayer() 

public void nextStage()

public int getStage()

public LocalDateTime getRunStartTime()

public int getDeathCount()

public void incrementDeathCount() *Only increments death count. A player running out of lives, and the decrement of their lives should be handled by GameManger*

public DifficultyEnum getDifficulty()

## GameRun implements GameRunInterface
*Stores information relating to a single run of the game*
### Constructors
public GameRun(DifficultyEnum difficulty) *creates a fresh game run*
### Fields
private EncounterInterface[] phase1NormalEncounters *drawn from for stages 1 and 2*

private EncounterInterface[] phase2NormalEncounters *drawn from for stages 4 and 5*

private EncounterInterface[] phase3NormalEncounters *drawn from for stages 7 and 8*

private EncounterInterface phase1Boss *drawn from for stage 3*

private EncounterInterface phase2Boss *drawn from for stage 6*

private EncounterInterface phase3Boss *drawn from for stage 9*

private EncounterInterface finalBoss *drawn from for stage 10*

private UpgradeEnum[] shopUpgrades *drawn from to populate the shop, removed from when an upgrade is bought*

private PlayerInterface player

private int currentStage

private DifficultyEnum currentDifficulty
## TelemetryListenerInterface
*Interface for the telemetry listener*
### Methods
public void onStartSession(StartSessionEvent e)

public void onNormalEncounterStart(NormalEncounterStartEvent e)

public void onNormalEncounterComplete(NormalEncounterCompleteEvent e)

public void onNormalEncounterFail(NormalEncounterFailEvent e)

public void onBossEncounterStart(BossEncounterStartEvent e)

public void onBossEncounterComplete(BossEncounterCompleteEvent e)

public void onBossEncounterFail(BossEncounterFailEvent e)

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
public TelemetryListenerInterface getInstance()

## abstract TelemetryEvent extends EventObject
*Contains fields all telemetry events contain*
### Constructors
public TelemetryEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName)
### Fields
private final int userID

private final int sessionID

private final String timestamp

private final String telemetryName
### Methods
public int getUserID()

public int getSessionID()

public String getTimestamp()

public String getTelemetryName() *gets the name of the telemetry event according to the specification*
## abstract EncounterEvent extends TelemetryEvent
*Contains information relating to encounters*
### COnstructors
public EncounterEvent(Object source, int userId, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty)
### Fields
private final int stageNumber

private final EncounterEnum encounterName

private final DifficultyEnum difficulty
### Methods
public int getStageNumber()

public EncounterEnum getEncounterName()

public DifficultyEnum getDifficulty()

## abstract EncounterStartEvent extends EncounterEvent
*Stores information for telemetry events where an encounter is started*
### Constructors
public EncounterStartEvent(Object source, int userId, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty)

## abstract EncounterCompleteEvent extends EncounterEvent
*Stores information for telemetry events where an encounter is completed*
### Constructors
public EncounterCompleteEvent(Object source, int userId, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty, int playerHPRemaining)
### Fields
private int playerHPRemaining
### Methods
public int getPlayerHPRemaining()

## abstract EncounterFailEvent extends EncounterEvent
*Stores information for telemetry events for when an encounter is failed*
### Constructors
public EncounterFailEvent(Object source, int userId, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty, int livesLeft)
### Fields
private int livesLeft
### Methods
public int getLivesLeft()

## StartSessionEvent extends TelemetryEvent
*Contains fields for sessions start, sent when telemetry is enabled. When telemetry is enabled a new session ID is generated.*
### Constructors
public StartSessionEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName)

## NormalEncounterStartEvent extends EncounterStartEvent
*Contains fields for normal encounter start*
### Constructors
public NormalEncounterStartEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty)

## NormalEncounterCompleteEvent extends EncounterCompleteEvent
*Contains fields for normal encounter complete*
### Constructors
public NormalEncounterCompleteEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty, int playerHPRemaining)

## NormalEncounterFailEvent extends EncounterFailEvent
*Contains fields for normal encounter fail*
### Constructors
public NormalEncounterFailEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty, int livesLeft)

## BossEncounterStartEvent extends EncounterStartEvent
*Contains fields for boss encounter start*
### Constructors
public BossEncounterStartEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty)

## BossEncounterCompleteEvent extends EncounterCompleteEvent
*Contains fields for boss encounter complete*
### Constructors
public BossEncounterCompleteEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty, int playerHPRemaining)

## BossEncounterFailEvent extends EncounterFailEvent
*Contains fields for boss encounter fail*
### Constructors
public BossEncounterFailEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty, int livesLeft)

## GainCoinEvent extends EncounterEvent
*Contains fields for gain coin*
### Constructors
public GainCoinEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber,  DifficultyEnum difficulty, int coinsGained)
### Fields
private final int coinsGained

### Methods
public int getCoinsGained()

## BuyUpgradeEvent extends EncounterEvent
*Contains fields for buy upgrade*
### Constructors
public BuyUpgradeEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber,  DifficultyEnum difficulty, UpgradeEnum upgradeBought, int coinsSpent)
### Fields
private final int coinsSpent

private final UpgradeEnum upgradeBought

### Methods
public int getCoinsSpent()

public UpgradeEnum getUpgradeBought()

## EndSessionEvent extends TelemetryEvent
*Contains fields for end session, sent when telemetry is disabled.*
### Constructors
public EndSessionEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName)
## SettingsChangeEvent extends TelemetryEvent
*Contains fields for settings change*
### Constructors
public SettingsChangeEvent(iObject source, nt userID, int sessionID, String timestamp, SettingType setting, String settingValue)
### Fields
private final SettingType setting

private final String settingValue
### Methods
public SettingType getSetting()

public String getSettingValue()

## KillEnemyEvent extends EncounterEvent
*Contains fields for kill enemy*
### Constructors
public KillEnemyEvent(Object source, int userID, int sessionID, String timestamp, String telemetryName, EncounterEnum encounterName, int stageNumber, DifficultyEnum difficulty, EntityEnum enemyType)
### Fields
private final EntityEnum enemyType
### Methods
public EntityEnum getEnemyType()

## SettingsInterface
*Interface for settings*
### Methods
public void createNewUser(String username, String password) throws AuthenticationException *Username must consist only of alphanumeric characters and underscores. If the JSON DB is empty, the created user should have the developer role, and otherwise player role*

public void authenticateUser(String username, String password) throws AuthenticationException  

public RoleEnum getUserRole() throws AuthenticationException

public void setUserRole(String username, RoleEnum role) throws AuthenticationException *Throws the exception if lacking permission ( not a developer) or username doesn't exist*

public String getUsername() throws AuthenticationException

public int getSessionID() throws AuthenticationException

public int getUserID() throws AuthenticationException *performs a hash function on the username to generate their ID*

public bool isTelemetryEnabled() throws AuthenticationException

public int getMaxStageReached(DifficultyEnum difficulty) throws AuthenticationException

public float getEnemyMaxHealthMultiplier(DifficultyEnum difficulty) 

public int getPlayerMaxHealth(DifficultyEnum difficulty)

public float getUpgradePriceMultiplier(DifficultyEnum difficulty)

public float getEnemyDamageMultiplier(DifficultyEnum difficulty)

public int getStartingLives(DifficultyEnum difficulty)

public int getMaxMagic(DifficultyEnum difficulty)

public int getMagicRegenRate(DifficultyEnum difficulty)

public int getShopItemCount(DifficultyEnum difficulty)

public void setTelemetryEnabled(bool telemetryEnabled)

public void setMaxStageReached(DifficultyEnum difficulty, int maxStageReached)

public void setEnemyMaxHealthMultiplier(DifficultyEnum difficulty, float newEnemyMaxHealthMultiplier)

public void setPlayerMaxHealth(DifficultyEnum difficulty, int newPlayerMaxHealth)

public void setUpgradePriceMultiplier(DifficultyEnum difficulty, float newUpgradePriceMultiplier)

public void setEnemyDamageMultiplier(DifficultyEnum difficulty, float newEnemyDamageMultiplier)

public void setStartingLives(DifficultyEnum difficulty, int newStartingLives)

public void setMaxMagic(DifficultyEnum difficulty, int newMaxMagic)

public void setMagicRegenRate(DifficultyEnum difficulty, int newMagicRegenRate)

public void setShopItemCount(DifficultyEnum difficulty, int newShopItemCount)

## private Settings implements SettingsInterface nested in SettingsSingleton
*Stores user settings and statistics*
### Constructors
public Settings()
### Fields
private bool telemetryEnabled

private String username

private int sessionID *generated when the user logs in or when telemetry is enabled*

private RoleEnum userRole

private EnumMap<\DifficultyEnum, Integer> maxStageReached

private EnumMap<\DifficultyEnum, Integer> playerMaxHealth

private EnumMap<\DifficultyEnum, Float> enemyDamageMultiplier

private EnumMap<\DifficultyEnum, Float> enemyMaxHealthMultiplier

private EnumMap<\DifficultyEnum, Integer> startingLives

private EnumMap<\DifficultyEnum, Integer> maxMagic

private EnumMap<\DifficultyEnum, Integer> magicRegenRate

private EnumMap<\DifficultyEnum, Integer> shopItemCount

## SettingsSingleton
*Provides singleton access to the user's settings*
### Constructors
private SettingsSingleton()
### Fields
private static SettingsInterface settings
### Methods
pubic SettingsInterface getInstance()

## AuthenticationException extends Exception
*Exception for failed authentication*
### Constructors
public AuthenticationException()

public AuthenticationException(String message)

public AuthenticationException(String message, Throwable cause)

## enum RoleEnum
*Enumerates all user roles*
### Constructors
private RoleEnum(String JSONName)
### Fields
PLAYER, DESIGNER, DEVELOPER

private final String JSONName
### Methods
public String getJSONName

## EncounterInterface
*Interface for encounters*
### Methods
public EntityInterface[] getEnemies()

public bool isComplete()

public void markComplete()

public EncounterEnum getType()

public void resetEnemyHealth()

## Encounter implements EncounterInterface
*Stores all the enemies in an encounter, and information about said encounter*
### Constructors
public Encounter(EncounterEnum type)
### Fields
private EntityInterface[] enemies 

private bool completed

private EncounterEnum encounterType

## enum DamageEnum
*Enumerates all the type of damage*
### Fields
PHYSICAL, FIRE, WATER, THUNDER, ABSOLUTE

## enum AbilityEnum
*Enumerates the game's abilities*
### Constructors
private AbilityEnum(String description, int baseDamage, int magicCost, DamageEnum damageType)
### Fields
PUNCH, ABSOLUTE_PULSE, SLASH, WATER_JET, THUNDER_STORM, FIRE_BALL

private final String description

private final int baseDamage

private final int magicCost

private final DamageEnum damageType

### Methods

public String getDescription()

public int getBaseDamage()

public int getMagicCost()

public DamageEnum getDamageType()

public void execute(EntityInterface source, EntityInterface target)

## enum UpgradeEnum
*Enumerates the game's upgrades*
### Constructor
private UpgradeEnum(int price, Class<? extends UpgradeBase> upgradeClass, String telemetryName)
### Fields
ABSOLUTE_PULSE, SLASH, WATER_JET, THUNDER_STORM, FIRE_BALL, PHYSICAL_DAMAGE_RESISTANCE, FIRE_DAMAGE_RESISTANCE, WATER_DAMAGE_RESISTANCE, THUNDER_DAMAGE_RESISTANCE, IMPROVED_PHYSICAL_DAMAGE, IMPROVED_FIRE_DAMAGE, IMPROVED_WATER_DAMAGE, IMPROVED_THUNDER_DAMAGE

private final int price

private final Class<? extends UpgradeBase> upgradeClass

private final String telemetryName
### Methods
public int getPrice()

public PlayerInterface applyUpgrade(PlayerInterface player) throws IllegalStateException, IllegalArgumentException *done using reflection, throws IllegalState if reflection fails, throws IllegalArgument if the player is null.*

public String getTelemetryName()

## enum DifficultyEnum
*Enumerates the different levels of difficulty*
### Constructors 
private DifficultyEnum(String telemetryName)
### Fields
EASY, MEDIUM, HARD

private final String telemetryName
### Methods
public String getTelemetryName() *provides the name for the difficulty according to the telemetry schema*

## enum SettingsEnum
*Enumerates the names of the different game settings that are per user*
### Constructors
private SettingsEnum(String telemetryName)
### Fields
TELEMETRY_ENABLED

private final String telemetryName
### Methods
public String getTelemetryName()
## enum EncounterEnum
*Enumerates the games encounters*
### Constructors
private EncounterEnum(EntityEnum[] enemies, String telemetryName)
### Fields
ZOMBIE_HORDE, SKELETON_RANGERS, etc. *placeholder, encounters TBD*

private final EntityEnum[] enemies

private final String telemetryName
### Methods
public EntityEnum[] getEnemies()

public EncounterInterface createEncounter() *done using reflection via EntityEnum*

public String getTelemetryName()

## enum EntityEnum
*Enumerates the games entities*
### Constructor
private EntityEnum(Class<? extends EntityInterface> entityClass, String telemetryName)
### Fields
GOBLIN, FISH_MAN, PYROMANCER, EVIL_WIZARD, ARMOURED_GOBLIN, GHOST, BLACK_KNIGHT, DRAGON

private final Class<? extends EntityInterface> entityClass

private final String telemetryName
### Methods
public EntityInterface createEntity() throws IllegalStateException *Done using reflection*

public String getTelemetryName()
## interface EntityInterface
*Contains all methods all entities have*
### Methods
public void loseHealth(int amount, DamageEnum type) throws IllegalArgumentException *amount cannot be negative*

public int getHealth()

public int getMaxHealth()

public int calcDamage(int base, DamageEnum type) *applies modifiers to damage type and returns the result*

public List\<AbilityEnum> getAbilities()

public void resetHealth()

public EntityEnum getType()
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

public void loseLives(int amount) throws IllegalArgumentException *amount cannot be negative*

List\<UpgradeEnum> getUpgrades()
## Player implements PlayerInterface
*Contains the concrete implementation of the player*
### Constructors
public Player(DifficultyEnum difficulty) *reads values from settings, and adjusting it's statistics using them (e.g. health)*
### Fields
private int maxMagic

private int maxHealth

private int health

private int magic

private int coins

private int lives

private DifficultyEnum difficulty

## abstract EnemyBase implements EntityInterface
*Contains an implementation of health points that all enemies use*
### Constructors
public Enemy(int maxHealth) *specified by the ConcreteEnemy*
### Fields
private int health

private int maxHealth

## ConcreteEnemy extends EnemyBase
*Each enemy would have its own class, for example SkeletonEnemy, ZombieEnemy*
### Constructors
public ConcreteEnemy(DifficultyEnum difficulty) *reads values from settings, and adjusting it's statistics using them (e.g. health)*

## LackingResourceException extends Exception
*Exception for when an ability is attempted without sufficient resources*
### Constructors
public LackingResourceException()

public LackingResourceException(String message)

public LackingResourceException(String message, Throwable cause)

## ConcreteUpgrade extends UpgradeBase
*Decorates the player, giving their methods additional abilities and effects*
### Constructors
public ConcreteUpgrade(PlayerInterface player)

## abstract UpgradeBase implements PlayerInterface 
*Holds the player instance and forwards method calls to them*
### Constructors 
public UpgradeBase(PlayerInterface player) throws IllegalArgumentException *if the given player is null*
### Fields 
protected PlayerInterface player

