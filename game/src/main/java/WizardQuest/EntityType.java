package WizardQuest;


/**
 * Enumerates all entity types in the game.
 */

public enum EntityType {
    PLAYER(Player.class, "Player"),
    GOBLIN(Goblin.class, "Goblin"), // enemy 1
    FISH_MAN(FishMan.class, "Fish Man"), // enemy 2
    PYROMANCER(null, "Pyromancer"), // PLACEHOLDER ARGS
    EVIL_WIZARD(null, "Evil Wizard"), // PLACEHOLDER ARGS
    ARMOURED_GOBLIN(null, "Armoured Goblin"), // PLACEHOLDER ARGS
    GHOST(null, "Ghost"), // PLACEHOLDER ARGS
    BLACK_KNIGHT(null, "Black Knight"), // PLACEHOLDER ARGS
    DRAGON(null, "Dragon"),; // PLACEHOLDER ARGS

    private final Class<? extends EntityInterface> enemyClass;
    private final String telemetryName;

    /**
     * Constructor for the enum EntityType
     * @param enemyClass the class object for instantiation using reflection
     * @param telemetryName the naming for the telemetry events
     */
    private EntityType(Class<? extends EntityInterface> enemyClass, String telemetryName) {
        this.enemyClass = enemyClass;
        this.telemetryName = telemetryName;
    }

    /**
     * Creates a new instance of this entity using Java reflection
     * @return a new instance EntityInterface of this type
     */
    public EntityInterface createEnemy() {
//        try {
//            return enemyClass.getDeclaredConstructor().newInstance();
//        } catch (InstantiationException | NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        } to be fixed
        return null;
    }

    /**
     * This method gets the telemetry name for this entity type
     * @return tje class object for this entity
     */
    public String getTelemetryName() {
        return telemetryName;
    }
}

