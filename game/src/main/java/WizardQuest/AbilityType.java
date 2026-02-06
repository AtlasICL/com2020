package WizardQuest;

import java.lang.reflect.Constructor;

public enum AbilityType{
    //organised by description, damage type, base damage, base magic points
    PUNCH(
        "A simple punch.",
        10,
        0,
        DamageType.PHYSICAL
    ), 
    ABSOLUTE_PULSE(
        "A magical pulse that no-one can defend against.",
        15,
        20,
        DamageType.ABSOLUTE
    ),
    SLASH(
        "A slash with a blade.",
        20,
        0,
        DamageType.PHYSICAL
    ),
    WATER_JET(
        "A magical (and high-velocity) jet of water.",
        15,
        20,
        DamageType.WATER
    ),
    THUNDER_STORM(
        "A magical blast of electricity.",
        25,
        30,
        DamageType.THUNDER
    ),
    FIRE_BALL(
        "A magically summoned powerful ball of fire.",
        35,
        40,
        DamageType.FIRE
    );

    private final String description;
    private final int baseDamage;
    private final int baseMagicPoints;
    private final DamageType damageType;

    AbilityType(
        String description,
        int baseDamage,
        int baseMagicPoints,
        DamageType damageType
    ) {
        this.description = description;
        this.baseDamage = baseDamage;
        this.baseMagicPoints = baseMagicPoints;
        this.damageType = damageType;
    }

    public String getDescription() {
        return description;
    }

    public AbilityType getType() {
        return type;
    }

    public final void execute(EntityInterface source, EntityInterface target) throws LackingResourceException{
        if (source instanceof PlayerInterface player) {
            if (player.getMagic() >= baseMagicPoints) {
                player.loseMagic(baseMagicPoints);
            } else {
                throw new LackingResourceException("Not enough magic points");
            }
        } //no magic consumed if enemy uses attack
        target.loseHealth(baseDamage, damageType);
    }
}