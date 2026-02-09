package WizardQuest;

import java.util.List;

public class PunchUnlockUpgrade extends UpgradeBase {
    public PunchUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List<UpgradeType> u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.PUNCH_UNLOCK)){
            u.add(UpgradeType.PUNCH_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List<AbilityType> a = super.player.getAbilities();
        if (!a.contains(AbilityType.PUNCH)){
            a.add(AbilityType.PUNCH);
        }
        return a;
    }
}