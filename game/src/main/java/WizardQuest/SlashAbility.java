public class SlashAbility extends ConcreteAbility {
    static {
        description = "A slash with a blade.";
        getNumberOfTargets = 1;
        type = AbilityType.SLASH;
    }
    private static final int baseDamage = 20;
    private static final int baseMagicPoints = 0;
    private static final int damageType = DamageType.PHYSICAL;
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        useAbility(source, targets[0], baseDamage, baseMagicPoints, damageType);
    }
}