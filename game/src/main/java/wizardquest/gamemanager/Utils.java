package wizardquest.gamemanager;

/* Utils class, providing utility constants and functions
 */
public class Utils {
    public static final int COINS_GAINED = 25;

    /**
     * Determines if an encounter is a boss encounter based on which stage it
     * originated from.
     * 
     * @param stage the stage the encounter was drawn from
     * @return whether it is a boss encounter or not. 
     */
    public static boolean isBossEncounter(int stage) {
        return (stage == 3) || (stage == 6) || (stage == 9) || (stage == 10);
    }
}
