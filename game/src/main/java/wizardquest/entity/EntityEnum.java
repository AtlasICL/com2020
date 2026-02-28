package wizardquest.entity;

import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonValue;

import wizardquest.settings.DifficultyEnum;

/**
 * Enumerates all entity types in the game.
 * Each of the types in this class has a reference to their concrete class used
 * for instantiation, done via reflection
 */
public enum EntityEnum {
    PLAYER(Player.class, "Player", "Player"),
    GOBLIN(Goblin.class, "Goblin", "Goblin"),
    FISH_MAN(FishMan.class, "Fishman", "Fish Man"),
    PYROMANCER(Pyromancer.class, "Pyromancer", "Pyromancer"),
    EVIL_WIZARD(EvilWizard.class, "EvilWizard", "Evil Wizard"),
    ARMOURED_GOBLIN(ArmouredGoblin.class, "ArmouredGoblin", "Armoured Goblin"),
    GHOST(Ghost.class, "Ghost", "Ghost"),
    BLACK_KNIGHT(BlackKnight.class, "BlackKnight", "Black Knight"),
    DRAGON(Dragon.class, "Dragon", "Dragon");

    private final Class<? extends EntityInterface> enemyClass;
    private final String telemetryName;
    private final String displayName;

    /**
     * Constructor for the enum EntityType
     *
     * @param enemyClass    the class object for instantiation using reflection
     * @param telemetryName the naming for the telemetry events
     */
    private EntityEnum(Class<? extends EntityInterface> enemyClass, String telemetryName, String displayName) {
        this.enemyClass = enemyClass;
        this.telemetryName = telemetryName;
        this.displayName = displayName;
    }

    /**
     * Creates a new instance of this entity using Java reflection
     *
     * @param difficulty the current difficulty of the game
     * @return a new instance EntityInterface of this type
     * @throws IllegalStateException this happens if something goes wrong with
     *                               reflection
     */
    public EntityInterface createEnemy(DifficultyEnum difficulty) {
        try {
            return enemyClass.getDeclaredConstructor(DifficultyEnum.class).newInstance(difficulty);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * This method gets the telemetry name for this entity type
     *
     * @return the class object for this entity
     */
    @JsonValue
    public String getTelemetryName() {
        return telemetryName;
    }

    /**
     * This method gets the telemetry name for this entity type
     *
     * @return the class object for this entity
     */
    public String getDisplayName() {
        return displayName;
    }
}
