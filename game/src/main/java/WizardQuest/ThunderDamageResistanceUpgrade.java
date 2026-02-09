package WizardQuest;

import java.util.List;

public class ThunderDamageResistanceUpgrade extends UpgradeBase {

    public ThunderDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageType type) {
        if (type == DamageType.THUNDER) {
            super.player.loseHealth(amount / 2, type);
        } else {
            super.player.loseHealth(amount, type);
        }
    }

    @Override
    public List<UpgradeType> getUpgrades() {
        List<UpgradeType> u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.THUNDER_DAMAGE_RESISTANCE)) {
            u.add(UpgradeType.THUNDER_DAMAGE_RESISTANCE);
        }
        return u;
    }
}