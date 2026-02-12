package WizardQuest;

import java.util.LinkedList;
import java.util.List;

/**
 * Goblin: beginner level enemy
 * They have low health and use only physical attacks
 * Phase 1 enemy
 */
public class Goblin extends EnemyBase {
    /**
     *
     * @param difficulty defines the current game difficulty for this concrete enemy, used to scale health
     */
    public Goblin(Difficulty difficulty){
        super((int) Math.round(50 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }
    @Override
    public List<AbilityType> getAbilities(){
        return new LinkedList<>(List.of(AbilityType.SLASH, AbilityType.PUNCH));
    }
    @Override
    public EntityEnum getType(){
        return EntityEnum.GOBLIN;
    }
    @Override
    public int calcDamage(int base, DamageType type){
        return base; // Goblin has no resistance
    }

}
