public class AbsolutePulseAbility extends ConcreteAbility {
    static {
        description = "A magical pulse that no-one can defend against.";
        getNumberOfTargets = 1;
        type = AbilityType.ABSOLUTE_PULSE;
    }
    private static final int baseDamage = 15;
    private static final int baseMagicPoints = 20;
    private static final int damageType = DamageType.ABSOLUTE;
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        useAbility(source, targets[0], baseDamage, baseMagicPoints, damageType);
    }
}