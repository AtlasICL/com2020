package wizardquest.entity;

import java.util.LinkedList;
import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;


public class EvilWizard extends EnemyBase {
    /**
     * @param difficulty defines the current game difficulty for this concrete
     *                   enemy, used to scale health
     */
    public EvilWizard(DifficultyEnum difficulty) {
        super(Math.round(50 * SettingsSingleton.getInstance().getEnemyMaxHealthMultiplier(difficulty)));
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        LinkedList<AbilityEnum> abilities = new LinkedList<>();
        abilities.add(AbilityEnum.THUNDER_STORM);
        abilities.add(AbilityEnum.FIRE_BALL);
        return abilities;
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.EVIL_WIZARD;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type != DamageEnum.PHYSICAL && type != DamageEnum.ABSOLUTE) {
            amount = 0;
        }
        super.loseHealth(amount, type);
    }
}
