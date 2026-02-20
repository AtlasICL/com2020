package wizardquest.abilities;

import java.util.List;

import wizardquest.entity.PlayerInterface;

public class FireDamageResistanceUpgrade extends UpgradeBase {

    public FireDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.FIRE) {
            super.player.loseHealth(Math.round(amount / 2), type);
        } else {
            super.player.loseHealth(amount, type);
        }
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.FIRE_DAMAGE_RESISTANCE)) {
            u.add(UpgradeEnum.FIRE_DAMAGE_RESISTANCE);
        }
        return u;
    }
}