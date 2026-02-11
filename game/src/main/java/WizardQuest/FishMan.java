package WizardQuest;

import java.util.LinkedList;
import java.util.List;

/**
 * Fishman - is a water-based beginner-level enemy
 * Uses water based techniques, and is water damage resistant
 * Phase 1 enemy
 */
public class FishMan extends Enemy {
    /**
     *
     * @param difficulty defines the current game difficulty for this concrete enemy, used to scale health
     */
    public FishMan(Difficulty difficulty){
        super((int) Math.round(50 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
        setWaterDamageModifier(0.5f);
        setThunderDamageModifier(1.5f);
    }
    @Override
    public List<AbilityType> getAbilities(){
        return new LinkedList<>(List.of(AbilityType.WATER_JET, AbilityType.PUNCH));
    }
    @Override
    public EntityType getType(){
        return EntityType.FISH_MAN;
    }
}

