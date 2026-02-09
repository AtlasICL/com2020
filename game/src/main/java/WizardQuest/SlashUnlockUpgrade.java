package WizardQuest;

import java.util.List;

public class SlashUnlockUpgrade extends UpgradeBase {
    public SlashUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List<UpgradeType> u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.SLASH_UNLOCK)){
            u.add(UpgradeType.SLASH_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List<AbilityType> a = super.player.getAbilities();
        if (!a.contains(AbilityType.SLASH)){
            a.add(AbilityType.SLASH);
        }
        return a;
    }
}