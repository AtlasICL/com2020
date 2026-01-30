public class FireBallAbility extends ConcreteAbility {
    static {
        description = "A magically summoned powerful ball of fire.";
        getNumberOfTargets = 1;
        type = AbilityType.FIRE_BALL;
    }
    private static final int baseDamage = 35;
    private static final int baseMagicPoints = 40;
    private static final int damageType = DamageType.FIRE;
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        useAbility(source, targets[0], baseDamage, baseMagicPoints, damageType);
    }
}