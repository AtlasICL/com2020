public class SlashAbility extends ConcreteAbility {
    static {
        description = "A slash with a blade.";
        getNumberOfTargets = 1;
        type = AbilityType.SLASH;
    }
    //Base Damage = 20
    @Override
    public void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException{
        //TODO: implement me
    }
}