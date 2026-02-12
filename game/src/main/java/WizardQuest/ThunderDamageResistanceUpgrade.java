package WizardQuest;

import java.util.List;

public class ThunderDamageResistanceUpgrade extends UpgradeBase {

    public ThunderDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.THUNDER) {
            super.player.loseHealth(amount / 2, type);
        } else {
            super.player.loseHealth(amount, type);
        }
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.THUNDER_DAMAGE_RESISTANCE)) {
            u.add(UpgradeEnum.THUNDER_DAMAGE_RESISTANCE);
        }
        return u;
    }
}