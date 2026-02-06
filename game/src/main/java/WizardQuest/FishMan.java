package WizardQuest;

/**
 * Fishman - is a water-based beginner-level enemy
 * Uses water based techniques, and is water damage resistant
 * Phase 1 enemy
 */
public class FishMan extends Enemy {

    static final int base_health = 60;

    public FishMan(){
        super();
        setMaxHealth(base_health);
    }

    @Override
    public EntityType getType(){
        return EntityType.FISH_MAN;
    }
}

