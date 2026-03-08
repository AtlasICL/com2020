package wizardquest.entity;

import java.util.LinkedList;
import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;

/**
 * Ghost - is a gimmick based boss enemy
 * Uses many techniques, and is invulnerable to physical damage, but weak to absolute damage
 * Phase 1 enemy
 */
public class Ghost extends EnemyBase {
    /**
     * @param difficulty defines the current game difficulty for this concrete
     *                   enemy, used to scale health
     */
    public Ghost(DifficultyEnum difficulty) {
        super(Math.round(80 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        LinkedList<AbilityEnum> abilities = new LinkedList<>();
        abilities.add(AbilityEnum.THUNDER_STORM);
        abilities.add(AbilityEnum.ABSOLUTE_PULSE);
        abilities.add(AbilityEnum.FIRE_BALL);
        return abilities;
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.GHOST;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.PHYSICAL) {
            amount = 0;
        } else if (type == DamageEnum.ABSOLUTE) {
            amount = amount * 2;
        }
        super.loseHealth(amount, type);
    }
}
