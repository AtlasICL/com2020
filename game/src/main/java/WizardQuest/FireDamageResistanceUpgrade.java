package WizardQuest;

public class FireDamageResistanceUpgrade extends ConcreteUpgrade {

    public FireDamageResistanceUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.FIRE){
            player.loseHealth(amount/2, type);   
        } 
        else {
            player.loseHealth(amount, type); 
        }
    }
}