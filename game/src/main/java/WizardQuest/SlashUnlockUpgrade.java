package WizardQuest;

import java.util.List;

public class SlashUnlockUpgrade extends UpgradeBase {
    public SlashUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeEnum> getUpgrades(){
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.SLASH_UNLOCK)){
            u.add(UpgradeEnum.SLASH_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityEnum> getAbilities(){
        List<AbilityEnum> a = super.player.getAbilities();
        if (!a.contains(AbilityEnum.SLASH)){
            a.add(AbilityEnum.SLASH);
        }
        return a;
    }
}