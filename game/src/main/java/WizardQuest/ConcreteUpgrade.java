package WizardQuest;

public abstract class ConcreteUpgrade extends Player {
    private final PlayerInterface player;

    public ConcreteUpgrade(PlayerInterface player) {
        this.player = player;
    }
}