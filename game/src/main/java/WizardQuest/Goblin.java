package WizardQuest;

/**
 * Goblin: beginner level enemy
 * They have low health and use only physical attacks
 * Phase 1 enemy
 */
public class Goblin extends Enemy{
    static final int base_max_health = 50;

    public Goblin(){
        super();
        setMaxHealth(base_max_health);
    }

    @Override
    public EntityType getType(){
        return EntityType.GOBLIN;
    }
}
