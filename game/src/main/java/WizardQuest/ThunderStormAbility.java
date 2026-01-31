public class ThunderStorm extends ConcreteAbility {
    static {
        description = "A magical blast of electricity.";
        getNumberOfTargets = 1;
        type = AbilityType.THUNDER_STORM;
    }
    private static final int baseDamage = 25;
    private static final int baseMagicPoints = 30;
    private static final int damageType = DamageType.THUNDER;
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        useAbility(source, targets[0], baseDamage, baseMagicPoints, damageType);
    }
}