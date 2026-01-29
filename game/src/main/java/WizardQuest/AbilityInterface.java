public interface AbilityInterface {
    void execute(EntityInterface source, EntityInterface[] targets) throws LackingResourceException;
    String getDescription();  
    int getNumberOfTargets();
    AbilityType getType();
}