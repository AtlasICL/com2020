public class PhysicalDamageResistanceUpgrade extends UpgradeBase {

    public PhysicalDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.PHYSICAL){
            return super.player.loseHealth(amount/2, type);   
        } 
        else {
            return super.player.loseHealth(amount, type); 
        }
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.PHYSICAL_DAMAGE_RESISTANCE)){
            u.add(UpgradeType.PHYSICAL_DAMAGE_RESISTANCE);
        }
        return u;
    }
}