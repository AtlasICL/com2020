package wizardquest.entity;

import java.util.ArrayList;
import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;

/**
 * Goblin: beginner level enemy
 * They have low health and use only physical attacks
 * Phase 1 enemy
 */
public class Goblin extends EnemyBase {
    /**
     * @param difficulty defines the current game difficulty for this concrete enemy, used to scale health
     */
    public Goblin(DifficultyEnum difficulty){
        super(Math.round(50 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }

    @Override
    public List<AbilityEnum> getAbilities(){
        List<AbilityEnum> abilities = new ArrayList<>();
        abilities.add(AbilityEnum.SLASH);
        abilities.add(AbilityEnum.PUNCH);
        return abilities;
    }

    @Override
    public EntityEnum getType(){
        return EntityEnum.GOBLIN;
    }

}
