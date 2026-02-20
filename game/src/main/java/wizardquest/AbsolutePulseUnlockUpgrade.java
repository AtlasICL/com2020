package wizardquest;

import java.util.List;

public class AbsolutePulseUnlockUpgrade extends UpgradeBase {
    public AbsolutePulseUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeEnum> getUpgrades(){
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.ABSOLUTE_PULSE_UNLOCK)){
            u.add(UpgradeEnum.ABSOLUTE_PULSE_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityEnum> getAbilities(){
        List<AbilityEnum> a = super.player.getAbilities();
        if (!a.contains(AbilityEnum.ABSOLUTE_PULSE)){
            a.add(AbilityEnum.ABSOLUTE_PULSE);
        }
        return a;
    }
}