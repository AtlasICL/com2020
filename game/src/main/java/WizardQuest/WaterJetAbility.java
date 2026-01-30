public class WaterJetAbility extends ConcreteAbility {
    static {
        description = "A magical (and high-velocity) jet of water.";
        getNumberOfTargets = 1;
        type = AbilityType.WATER_JET;
    }
    private static final int baseDamage = 15;
    private static final int baseMagicPoints = 20;
    private static final int damageType = DamageType.WATER;
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        useAbility(source, targets[0], baseDamage, baseMagicPoints, damageType);
    }
}