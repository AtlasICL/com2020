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
        LinkedList<AbilityType> abilities = new LinkedList<>();
        abilities.add(AbilityType.PUNCH);
        abilities.add(AbilityType.WATER_JET);
        return abilities;
    }
    @Override
    public EntityEnum getType(){
        return EntityEnum.FISH_MAN;
    }

    @Override
    public void loseHealth(int amount, DamageType type){
        if (type == DamageType.THUNDER){
            amount = (int) Math.round(amount * 2.0);
        }
        else if (type == DamageType.WATER){
            amount = (int) Math.round(amount / 2.0);
        }
        super.loseHealth(amount, type);
    }
}

