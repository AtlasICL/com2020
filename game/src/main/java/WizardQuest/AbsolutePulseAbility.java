public class AbsolutePulseAbility extends ConcreteAbility {
    static {
        description = "A magical pulse that no-one can defend against.";
        getNumberOfTargets = 1;
        type = AbilityType.ABSOLUTE_PULSE;
    }
    //Base Damage = 15
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        //TODO: implement me
    }
}