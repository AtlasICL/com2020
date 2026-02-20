package wizardquest;

public class Encounter implements EncounterInterface {
    private EntityInterface[] enemies;
    private boolean completed;
    private final EncounterEnum type;

    public Encounter(EncounterEnum type, DifficultyEnum difficulty) {

        //never allow null encounters
        if (type == null) {
            throw new IllegalArgumentException("Encounter type cannot be null.");
        }

        this.type = type;

        //build enemies from enum
        EntityEnum[] enemyTypes = type.getEnemies();
        this.enemies = new EntityInterface[enemyTypes.length];

        for (int i = 0; i < enemyTypes.length; i++) {
            this.enemies[i] = enemyTypes[i].createEnemy(difficulty);
        }
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
    public EncounterEnum getType() {
        return type;
    }

    @Override
    public void resetEnemyHealth() {
        if (enemies == null) return;
        for (EntityInterface e : enemies) {
            if (e != null) e.resetHealth();
        }
    }
}
