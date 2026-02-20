package wizardquest;

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
    public FishMan(DifficultyEnum difficulty){
        super(Math.round(50 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }
    @Override
    public List<AbilityEnum> getAbilities(){
        LinkedList<AbilityEnum> abilities = new LinkedList<>();
        abilities.add(AbilityEnum.PUNCH);
        abilities.add(AbilityEnum.WATER_JET);
        return abilities;
    }
    @Override
    public EntityEnum getType(){
        return EntityEnum.FISH_MAN;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type){
        if (type == DamageEnum.THUNDER){
            amount = amount * 2;
        }
        else if (type == DamageEnum.WATER){
            amount = Math.round(amount / 2.0f);
        }
        super.loseHealth(amount, type);
    }
}

