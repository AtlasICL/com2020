package WizardQuest;

import java.util.List;

public class WaterDamageResistanceUpgrade extends UpgradeBase {

    public WaterDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageType type) {
        if (type == DamageType.WATER){
            super.player.loseHealth(amount/2, type);   
        } 
        else {
            super.player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List<UpgradeType> u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.WATER_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.WATER_DAMAGE_RESISTANCE);
        }
        return u;
    }
}