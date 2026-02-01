public class FireDamageResistanceUpgrade extends Player {

    public FireDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.FIRE){
            return player.loseHealth(amount/2, type);   
        } 
        else {
            return player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.FIRE_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.FIRE_DAMAGE_RESISTANCE);
        }
        return u;
    }
}