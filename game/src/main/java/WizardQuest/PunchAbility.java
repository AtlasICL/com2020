public class PunchAbility extends ConcreteAbility {
    static {
        description = "A simple punch.";
        getNumberOfTargets = 1;
        type = AbilityType.PUNCH();
    }

    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        //TODO: implement me
    }
}