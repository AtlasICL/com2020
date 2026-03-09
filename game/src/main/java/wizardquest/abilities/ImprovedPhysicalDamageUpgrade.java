package wizardquest.abilities;

import java.util.List;

import wizardquest.entity.PlayerInterface;
/**
 * Improved Physical Damage Upgrade - upgrade that increases the player's physical damage.
 */
public class ImprovedPhysicalDamageUpgrade extends UpgradeBase {

    public ImprovedPhysicalDamageUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int calcDamage(int amount, DamageEnum type) {
        if (type == DamageEnum.PHYSICAL) {
            return super.player.calcDamage(amount * 2, type);
        }
        return super.player.calcDamage(amount, type);
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.IMPROVED_PHYSICAL_DAMAGE)) {
            u.add(UpgradeEnum.IMPROVED_PHYSICAL_DAMAGE);
        }
        return u;
    }
}
