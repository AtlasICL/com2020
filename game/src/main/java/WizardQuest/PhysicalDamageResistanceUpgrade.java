public class PhysicalDamageResistanceUpgrade extends UpgradeBase {

    public PhysicalDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public void loseHealth(int amount, DamageType type) {
        if (type == DamageType.PHYSICAL){
            super.player.loseHealth(amount/2, type);   
        } 
        else {
            super.player.loseHealth(amount, type); 
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