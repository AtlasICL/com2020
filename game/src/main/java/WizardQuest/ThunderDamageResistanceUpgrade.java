public class ThunderDamageResistanceUpgrade extends Player {

    public ThunderDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.THUNDER){
            return player.loseHealth(amount/2, type);   
        } 
        else {
            return player.loseHealth(amount, type); 
        }
    }

        @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.THUNDER_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.THUNDER_DAMAGE_RESISTANCE);
        }
        return u;
    }
}