import javax.swing.text.html.parser.Entity;

public class PunchAbility extends ConcreteAbility {
    static {
        description = "A simple punch.";
        getNumberOfTargets = 1;
        type = AbilityType.PUNCH;
    }
    private static final int baseDamage = 10;
    private static final int baseMagicPoints = 0;
    private static final int damageType = DamageType.PHYSICAL;
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        useAbility(source, targets[0], baseDamage, baseMagicPoints, damageType);
    }
}