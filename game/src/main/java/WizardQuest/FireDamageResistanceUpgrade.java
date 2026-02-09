import java.util.List;

public class FireDamageResistanceUpgrade extends UpgradeBase {

    public FireDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageType type) {
        if (type == DamageType.FIRE){
            super.player.loseHealth(amount/2, type);   
        } 
        else {
            super.player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List<UpgradeType> u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.FIRE_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.FIRE_DAMAGE_RESISTANCE);
        }
        return u;
    }
}