package wizardquest;

import java.util.List;

public class WaterDamageResistanceUpgrade extends UpgradeBase {

    public WaterDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) {
        if (type == DamageEnum.WATER){
            super.player.loseHealth(Math.round(amount/2), type);   
        } 
        else {
            super.player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeEnum> getUpgrades(){
        List<UpgradeEnum> u = super.player.getUpgrades();
        if (!u.contains(UpgradeEnum.WATER_DAMAGE_RESISTANCE)){
            u.add(UpgradeEnum.WATER_DAMAGE_RESISTANCE);
        }
        return u;
    }
}