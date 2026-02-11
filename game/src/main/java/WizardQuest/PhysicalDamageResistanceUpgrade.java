package WizardQuest;

import java.util.List;

public class PhysicalDamageResistanceUpgrade extends UpgradeBase {

    public PhysicalDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.PHYSICAL) {
            super.player.loseHealth(amount / 2, type);
        } else {
            super.player.loseHealth(amount, type);
        }
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.PHYSICAL_DAMAGE_RESISTANCE)) {
            u.add(UpgradeEnum.PHYSICAL_DAMAGE_RESISTANCE);
        }
        return u;
    }
}