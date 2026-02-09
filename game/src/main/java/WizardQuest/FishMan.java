package WizardQuest;

import java.util.List;

/**
 * Fishman - is a water-based beginner-level enemy
 * Uses water based techniques, and is water damage resistant
 * Phase 1 enemy
 */
public class FishMan extends Enemy {



    public FishMan(){
        super((int) Math.round(60 * 1.0f));
    }
    @Override
    public List<AbilityType> getAbilities(){
        return List.of(AbilityType.WATER_JET, AbilityType.PUNCH);
    }
    @Override
    public EntityType getType(){
        return EntityType.FISH_MAN;
    }
}

