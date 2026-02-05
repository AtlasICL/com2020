public class ThunderDamageResistanceUpgrade extends ConcreteUpgrade {

    public ThunderDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.THUNDER){
            player.loseHealth(amount/2, type);   
        } 
        else {
            player.loseHealth(amount, type); 
        }
    }
}