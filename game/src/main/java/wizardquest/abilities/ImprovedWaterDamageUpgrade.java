package wizardquest.abilities;

import java.util.List;

import wizardquest.entity.PlayerInterface;

public class ImprovedWaterDamageUpgrade extends UpgradeBase {

    public ImprovedWaterDamageUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int calcDamage(int amount, DamageEnum type) {
        if (type == DamageEnum.WATER) {
            return super.player.calcDamage(amount * 2, type);
        }
        return super.player.calcDamage(amount, type);
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.IMPROVED_WATER_DAMAGE)) {
            u.add(UpgradeEnum.IMPROVED_WATER_DAMAGE);
        }
        return u;
    }
}
