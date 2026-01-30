public class ThunderStorm extends ConcreteAbility {
    static {
        description = "A magical blast of electricity.";
        getNumberOfTargets = 1;
        type = AbilityType.THUNDER_STORM;
    }
    //Base Damage = 25
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        //TODO: implement me
    }
}