package WizardQuest;

/**
 * Enumerates all ability types in the game.
 */
public enum AbilityType{
    PUNCH("Punch", -1, -1, null), // PLACEHOLDER ARGS
    ABSOLUTE_PULSE("Absolute Pulse", -1, -1, null), // PLACEHOLDER ARGS
    SLASH("Slash", -1, -1, null), // PLACEHOLDER ARGS
    WATER_JET("Water Jet", -1, -1, null), // PLACEHOLDER ARGS
    THUNDER_STORM("Thunder Storm", -1, -1, null), // PLACEHOLDER ARGS
    FIRE_BALL("Fire Ball", -1, -1, null); // PLACEHOLDER ARGS

    private final String description;
    private final int baseDamage;
    private final int magicCost;
    private final DamageType damageType;

    private AbilityType(String description, int baseDamage, int magicCost, DamageType damageType) {
        this.description = description;
        this.baseDamage = baseDamage;
        this.magicCost = magicCost;
        this.damageType = damageType;
    }

    public String getDescription() {
        return description;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getMagicCost() {
        return magicCost;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    /**
     * Called by a source entity executing their ability on a single target.
     *
     * @param source the entity executing the ability.
     * @param target the entity that is subject to an attack with this ability.
     */
    public void execute(EntityInterface source, EntityInterface target) {}
}