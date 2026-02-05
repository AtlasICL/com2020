public class FireDamageResistanceUpgrade extends UpgradeBase {

    public FireDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.FIRE){
            return super.player.loseHealth(amount/2, type);   
        } 
        else {
            return super.player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.FIRE_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.FIRE_DAMAGE_RESISTANCE);
        }
        return u;
    }
}