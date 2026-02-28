package wizardquest.entity;

import java.util.LinkedList;
import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;


public class Pyromancer extends EnemyBase {
    /**
     * @param difficulty defines the current game difficulty for this concrete
     *                   enemy, used to scale health
     */
    public Pyromancer(DifficultyEnum difficulty) {
        super(Math.round(30 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        LinkedList<AbilityEnum> abilities = new LinkedList<>();
        abilities.add(AbilityEnum.FIRE_BALL);
        return abilities;
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.PYROMANCER;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.WATER) {
            amount = amount * 2;
        } else if (type == DamageEnum.FIRE) {
            amount = Math.round(amount / 2.0f);
        }
        super.loseHealth(amount, type);
    }
}
