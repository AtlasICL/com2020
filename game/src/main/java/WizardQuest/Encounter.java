package WizardQuest;

public class Encounter implements EncounterInterface {
    private EntityInterface[] enemies;
    private boolean completed;
    private EncounterType type;

    public Encounter(EncounterType type) {
        this.type = type;
    }

    @Override
    public EntityInterface[] getEnemies() {
        return enemies;
    }

    @Override
    public boolean isComplete() {
        return completed;
    }

    @Override
    public void markComplete() {
        completed = true;
    }

    @Override
    public EncounterType getType() {
        return type;
    }

    @Override
    public void resetEnemyHealth() {}
}
