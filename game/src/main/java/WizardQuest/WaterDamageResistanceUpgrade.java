public class WaterDamageResistanceUpgrade extends Player {

    public WaterDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.WATER){
            player.loseHealth(amount/2, type);   
        } 
        else {
            player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.WATER_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.WATER_DAMAGE_RESISTANCE);
        }
        return u;
    }
}