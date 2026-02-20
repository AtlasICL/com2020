package wizardquest.abilities;

import java.util.List;

import wizardquest.entity.PlayerInterface;

public class FireBallUnlockUpgrade extends UpgradeBase {
    public FireBallUnlockUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.FIRE_BALL_UNLOCK)) {
            u.add(UpgradeEnum.FIRE_BALL_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        List<AbilityEnum> a = super.player.getAbilities();
        if (!a.contains(AbilityEnum.FIRE_BALL)) {
            a.add(AbilityEnum.FIRE_BALL);
        }
        return a;
    }
}