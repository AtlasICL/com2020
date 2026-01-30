public class WaterJetAbility extends ConcreteAbility {
    static {
        description = "A magical (and high-velocity) jet of water.";
        getNumberOfTargets = 1;
        type = AbilityType.WATER_JET;
    }
    //Base Damage = 15
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        //TODO: implement me
    }
}