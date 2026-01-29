package WizardQuest;

/**
 * Enumerates all ability types in the game.
 */
public enum AbilityType{
    PUNCH(), 
    ABSOLUTE_PULSE(),
    SLASH(),
    WATER_JET(),
    THUNDER_STORM(),
    FIRE_BALL();

    private final Class<? extends AbilityInterface> abiilityClass;

    private AbilityType(Class<? extends AbilityInterface> abiilityClass) {
        this.abiilityClass = abiilityClass;
    }
}