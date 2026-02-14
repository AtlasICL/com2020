package WizardQuest;


import java.lang.reflect.InvocationTargetException;

/**
 * Enumerates all entity types in the game.
 * Each of the types in this class has a reference to their concrete class used for instantiation, done via reflection
 */

public enum EntityEnum {
    PLAYER(Player.class, "Player"),
    GOBLIN(Goblin.class, "Goblin"), // enemy 1
    FISH_MAN(FishMan.class, "FishMan"), // enemy 2
    PYROMANCER(null, "Pyromancer"), // PLACEHOLDER ARGS
    EVIL_WIZARD(null, "EvilWizard"), // PLACEHOLDER ARGS
    ARMOURED_GOBLIN(null, "ArmouredGoblin"), // PLACEHOLDER ARGS
    GHOST(null, "Ghost"), // PLACEHOLDER ARGS
    BLACK_KNIGHT(null, "Black Knight"), // PLACEHOLDER ARGS
    DRAGON(null, "Dragon"),; // PLACEHOLDER ARGS

    private final Class<? extends EntityInterface> enemyClass;
    private final String telemetryName;

    /**
     * Constructor for the enum EntityType
     *
     * @param enemyClass the class object for instantiation using reflection
     * @param telemetryName the naming for the telemetry events
     */
    private EntityEnum(Class<? extends EntityInterface> enemyClass, String telemetryName) {
        this.enemyClass = enemyClass;
        this.telemetryName = telemetryName;
    }

    /**
     * Creates a new instance of this entity using Java reflection
     *
     * @param difficulty the current difficulty of the game
     * @return a new instance EntityInterface of this type
     * @throws IllegalStateException this happens if something goes wrong with reflection
     */
    public EntityInterface createEnemy(DifficultyEnum difficulty) {
        try {
            return enemyClass.getDeclaredConstructor(DifficultyEnum.class).newInstance(difficulty);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * This method gets the telemetry name for this entity type
     *
     * @return the class object for this entity
     */
    public String getTelemetryName() {
        return telemetryName;
    }
}

