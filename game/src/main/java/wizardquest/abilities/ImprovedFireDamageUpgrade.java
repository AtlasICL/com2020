package wizardquest.abilities;

import java.util.List;

import wizardquest.entity.PlayerInterface;
/**
 * Improved Fire Damage Upgrade - upgrade that increases the player's fire damage.
 */
public class ImprovedFireDamageUpgrade extends UpgradeBase {

    public ImprovedFireDamageUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int calcDamage(int amount, DamageEnum type) {
        if (type == DamageEnum.FIRE) {
            return super.player.calcDamage(amount * 2, type);
        }
        return super.player.calcDamage(amount, type);
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.IMPROVED_FIRE_DAMAGE)) {
            u.add(UpgradeEnum.IMPROVED_FIRE_DAMAGE);
        }
        return u;
    }
}
