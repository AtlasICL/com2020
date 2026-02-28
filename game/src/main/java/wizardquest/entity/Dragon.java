package wizardquest.entity;

import java.util.LinkedList;
import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;


public class Dragon extends EnemyBase {
    /**
     * @param difficulty defines the current game difficulty for this concrete
     *                   enemy, used to scale health
     */
    public Dragon(DifficultyEnum difficulty) {
        super(Math.round(120 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        LinkedList<AbilityEnum> abilities = new LinkedList<>();
        abilities.add(AbilityEnum.SLASH);
        abilities.add(AbilityEnum.FIRE_BALL);
        abilities.add(AbilityEnum.THUNDER_STORM);
        return abilities;
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.DRAGON;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.ABSOLUTE) {
            amount = amount * 2;
        } else if (type == DamageEnum.FIRE || type == DamageEnum.THUNDER) {
            amount = Math.round(amount / 2.0f);
        }
        super.loseHealth(amount, type);
    }
}
