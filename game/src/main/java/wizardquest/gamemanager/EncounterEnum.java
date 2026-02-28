package wizardquest.gamemanager;

import wizardquest.entity.EntityEnum;
import wizardquest.settings.DifficultyEnum;

/**
 * Enumerates all encounter types in the game.
 */
public enum EncounterEnum {
    // Phase 1 normal encounters (1 enemy each)
    GOBLIN_ENCOUNTER(new EntityEnum[] { EntityEnum.GOBLIN }, "GoblinEncounter", "Goblin Encounter"),
    FISHMAN_ENCOUNTER(new EntityEnum[] { EntityEnum.FISH_MAN }, "FishManEncounter", "Fish Man Encounter"),
    PYROMANCER_ENCOUNTER(new EntityEnum[] { EntityEnum.PYROMANCER }, "PyromancerEncounter", "Pyromancer Encounter"),

    // Phase 1 boss
    EVIL_WIZARD_ENCOUNTER(new EntityEnum[] { EntityEnum.EVIL_WIZARD }, "EvilWizardEncounter", "Evil Wizard Encounter"),

    // Phase 2 normal encounters (up to 2 enemies)
    GOBLIN_DUO_ENCOUNTER(new EntityEnum[] { EntityEnum.GOBLIN, EntityEnum.GOBLIN }, "GoblinDuoEncounter", "Goblin Duo Encounter"),
    GOBLIN_FISHMAN_ENCOUNTER(new EntityEnum[] { EntityEnum.GOBLIN, EntityEnum.FISH_MAN }, "GoblinFishManEncounter", "Goblin & Fish Man Encounter"),
    ARMOURED_GOBLIN_ENCOUNTER(new EntityEnum[] { EntityEnum.ARMOURED_GOBLIN }, "ArmouredGoblinEncounter", "Armoured Goblin Encounter"),
    PYROMANCER_FISHMAN_ENCOUNTER(new EntityEnum[] { EntityEnum.PYROMANCER, EntityEnum.FISH_MAN }, "PyromancerFishManEncounter", "Pyromancer & Fish Man Encounter"),

    // Phase 2 boss
    GHOST_ENCOUNTER(new EntityEnum[] { EntityEnum.GHOST }, "GhostEncounter", "Ghost Encounter"),

    // Phase 3 normal encounters (up to 3 enemies)
    ARMOURED_GOBLIN_PYROMANCER_ENCOUNTER(new EntityEnum[] { EntityEnum.ARMOURED_GOBLIN, EntityEnum.PYROMANCER }, "ArmouredGoblinPyromancerEncounter", "Armoured Goblin & Pyromancer Encounter"),
    GOBLIN_FISHMAN_PYROMANCER_ENCOUNTER(new EntityEnum[] { EntityEnum.GOBLIN, EntityEnum.FISH_MAN, EntityEnum.PYROMANCER }, "GoblinFishManPyromancerEncounter", "Goblin, Fish Man & Pyromancer Encounter"),

    // Phase 3 boss
    BLACK_KNIGHT_ENCOUNTER(new EntityEnum[] { EntityEnum.BLACK_KNIGHT }, "BlackKnightEncounter", "Black Knight Encounter"),

    // Final boss
    DRAGON_ENCOUNTER(new EntityEnum[] { EntityEnum.DRAGON }, "DragonEncounter", "Dragon Encounter");

    private final EntityEnum[] enemies;
    private final String telemetryName;
    private final String displayName;

    /**
     * Constructor for EncounterEnum
     *
     * @param enemies       array of EntityType objects that appear occur an
     *                      encounter
     * @param telemetryName the name used for telemetry events
     */
    private EncounterEnum(EntityEnum[] enemies, String telemetryName, String displayName) {
        this.enemies = enemies;
        this.telemetryName = telemetryName;
        this.displayName = displayName;
    }

    /**
     * Gets the types of enemies that occur during this Encounter
     *
     * @return an array of EntityType objects
     */
    public EntityEnum[] getEnemies() {
        return enemies;
    }

    /**
     * This creates a new instance of an Encounter of this type
     * Uses EntityType reflection for the instantiation of the actual encounters.
     * 
     * @return a new EncounterInterface with specified enemies
     */
    public EncounterInterface createEncounter(DifficultyEnum difficulty) {
        return new Encounter(this, difficulty);
    }

    /**
     * This method gets the telemetry name for this encounter
     * 
     * @return the telemetry name for the entity
     */
    public String getTelemetryName() {
        return telemetryName;
    }

    /**
     * This method gets the display name for this encounter
     * 
     * @return the display name for the entity
     */
    public String getDisplayName() {
        return displayName;
    }

}
