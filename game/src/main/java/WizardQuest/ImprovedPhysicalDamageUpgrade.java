package WizardQuest;

public class ImprovedPhysicalDamageUpgrade extends ConcreteUpgrade {

    public ImprovedPhysicalDamageUpgrade(PlayerInterface player) {
        super(player);
    }

    @Override
    public int loseHealth(int amount, DamageType type) {
        if (type == DamageType.PHYSICAL){
            player.loseHealth(amount/2, type);   
        } 
        else {
            player.loseHealth(amount, type); 
        }
    }
}