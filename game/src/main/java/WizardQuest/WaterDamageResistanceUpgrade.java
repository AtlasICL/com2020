public class WaterDamageResistanceUpgrade extends UpgradeBase {

    public WaterDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.WATER){
            return super.player.loseHealth(amount/2, type);   
        } 
        else {
            return super.player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.WATER_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.WATER_DAMAGE_RESISTANCE);
        }
        return u;
    }
}