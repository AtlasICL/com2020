package WizardQuest;

import java.util.LinkedList;
import java.util.List;

/**
 * Fishman - is a water-based beginner-level enemy
 * Uses water based techniques, and is water damage resistant
 * Phase 1 enemy
 */
public class FishMan extends EnemyBase {
    /**
     *
     * @param difficulty defines the current game difficulty for this concrete enemy, used to scale health
     */
    public FishMan(Difficulty difficulty){
        super((int) Math.round(50 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }
    @Override
    public List<AbilityType> getAbilities(){
        return new LinkedList<>(List.of(AbilityType.WATER_JET, AbilityType.PUNCH));
    }
    @Override
    public EntityEnum getType(){
        return EntityEnum.FISH_MAN;
    }

    @Override
    public int calcDamage(int base, DamageType type){
        if (type == DamageType.THUNDER){
            return Math.round(base * 2.0f);
        }
        else if (type == DamageType.WATER){
            return Math.round(base * 0.5f);
        }
        else{
            return base;
        }
    }
}

