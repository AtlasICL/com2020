package wizardquest.entity;

import java.util.LinkedList;
import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;

public class ArmouredGoblin extends EnemyBase {
    /**
     * @param difficulty defines the current game difficulty for this concrete
     *                   enemy, used to scale health
     */
    public ArmouredGoblin(DifficultyEnum difficulty) {
        super(Math.round(80 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        LinkedList<AbilityEnum> abilities = new LinkedList<>();
        abilities.add(AbilityEnum.SLASH);
        return abilities;
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.ARMOURED_GOBLIN;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.THUNDER) {
            amount = amount * 2;
        } else if (type == DamageEnum.PHYSICAL || type == DamageEnum.FIRE || type == DamageEnum.WATER) {
            amount = Math.round(amount / 2.0f);
        }
        super.loseHealth(amount, type);
    }
}
