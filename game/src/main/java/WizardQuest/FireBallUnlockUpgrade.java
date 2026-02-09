package WizardQuest;

import java.util.List;

public class FireBallUnlockUpgrade extends UpgradeBase {
    public FireBallUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List<UpgradeType> u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.FIRE_BALL_UNLOCK)){
            u.add(UpgradeType.FIRE_BALL_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List<AbilityType> a = super.player.getAbilities();
        if (!a.contains(AbilityType.FIRE_BALL)){
            a.add(AbilityType.FIRE_BALL);
        }
        return a;
    }
}