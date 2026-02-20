package wizardquest;

import java.util.List;

public class WaterJetUnlockUpgrade extends UpgradeBase {
    public WaterJetUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeEnum> getUpgrades(){
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.WATER_JET_UNLOCK)){
            u.add(UpgradeEnum.WATER_JET_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityEnum> getAbilities(){
        List<AbilityEnum> a = super.player.getAbilities();
        if (!a.contains(AbilityEnum.WATER_JET)){
            a.add(AbilityEnum.WATER_JET);
        }
        return a;
    }
}