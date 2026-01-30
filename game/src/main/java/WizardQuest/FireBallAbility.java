public class FireBallAbility extends ConcreteAbility {
    static {
        description = "A magically summoned powerful ball of fire.";
        getNumberOfTargets = 1;
        type = AbilityType.FIRE_BALL;
    }
    //Base Damage = 35
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        //TODO: implement me
    }
}