public class PhysicalDamageResistanceUpgrade extends Player {

    public PhysicalDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.PHYSICAL){
            return player.loseHealth(amount/2, type);   
        } 
        else {
            return player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.PHYSICAL_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.PHYSICAL_DAMAGE_RESISTANCE);
        }
        return u;
    }
}