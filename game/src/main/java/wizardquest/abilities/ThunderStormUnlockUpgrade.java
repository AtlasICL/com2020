package wizardquest.abilities;

import java.util.List;

import wizardquest.entity.PlayerInterface;
/**
 * Thunder Storm Unlock Upgrade - upgrade that unlocks the Thunder Storm attack.
 */
public class ThunderStormUnlockUpgrade extends UpgradeBase {
    public ThunderStormUnlockUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.THUNDER_STORM_UNLOCK)) {
            u.add(UpgradeEnum.THUNDER_STORM_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        List<AbilityEnum> a = super.player.getAbilities();
        if (!a.contains(AbilityEnum.THUNDER_STORM)) {
            a.add(AbilityEnum.THUNDER_STORM);
        }
        return a;
    }
}