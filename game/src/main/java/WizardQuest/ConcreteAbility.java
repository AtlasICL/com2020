public abstract class ConcreteAbility implements AbilityInterface{
    protected static String description;
    protected static int getNumberOfTargets;//to be removed
    protected static AbilityType type;

    protected ConcreteAbility() {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getNumberOfTargets() {
        return getNumberOfTargets;
    }

    @Override
    public AbilityType getType() {
        return type;
    }
}