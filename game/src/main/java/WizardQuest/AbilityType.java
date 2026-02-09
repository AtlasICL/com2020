package WizardQuest;

public enum AbilityType {
    // Organised by description, damage type, base damage, base magic points.
    PUNCH(
            "A simple punch.",
            10,
            0,
            DamageType.PHYSICAL),
    ABSOLUTE_PULSE(
            "A magical pulse that no-one can defend against.",
            15,
            20,
            DamageType.ABSOLUTE),
    SLASH(
            "A slash with a blade.",
            20,
            0,
            DamageType.PHYSICAL),
    WATER_JET(
            "A magical (and high-velocity) jet of water.",
            15,
            20,
            DamageType.WATER),
    THUNDER_STORM(
            "A magical blast of electricity.",
            25,
            30,
            DamageType.THUNDER),
    FIRE_BALL(
            "A magically summoned powerful ball of fire.",
            35,
            40,
            DamageType.FIRE);

    private final String description;
    private final int baseDamage;
    private final int baseMagicPoints;
    private final DamageType damageType;

    AbilityType(
            String description,
            int baseDamage,
            int baseMagicPoints,
            DamageType damageType) {
        this.description = description;
        this.baseDamage = baseDamage;
        this.baseMagicPoints = baseMagicPoints;
        this.damageType = damageType;
    }

    /**
     * Gets the description of the ability.
     * @return a human readable description of the ability.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the base damage for the ability, before any passive abilities (upgrades) modify it.
     * @return the base damage for the ability.
     */
    public int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Get the amount of magic this ability consumes when used.
     * @return the amount of magic the ability uses.
     */
    public int getMagicCost() {
        return baseMagicPoints;
    }

    /**
     * Get the type of damage the ability deals.
     * @return the ability's damage type.
     */
    public DamageType getDamageType() {
        return damageType;
    }

    /**
     * Executes the ability on a target.
     * @param source the entity using the ability.
     * @param target the entity the ability is attacking.
     * @throws LackingResourceException if the source doesn't have enough magic to use the ability.
     */
    public void execute(EntityInterface source, EntityInterface target) throws LackingResourceException {
        if (source instanceof PlayerInterface player) {
            if (player.getMagic() >= baseMagicPoints) {
                player.loseMagic(baseMagicPoints);
            } else {
                throw new LackingResourceException("Not enough magic points");
            }
        } // no magic consumed if enemy uses attack
        target.loseHealth(baseDamage, damageType);
    }
}