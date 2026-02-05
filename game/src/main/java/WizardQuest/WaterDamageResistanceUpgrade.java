package WizardQuest;

public class WaterDamageResistanceUpgrade extends ConcreteUpgrade {

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
}