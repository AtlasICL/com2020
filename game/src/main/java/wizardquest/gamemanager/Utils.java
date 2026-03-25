package wizardquest.gamemanager;

import java.util.Random;

import wizardquest.entity.EntityInterface;



/* Utils class, providing utility constants and functions
 */
public class Utils {
    private static final Random RANDOM = new Random();

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

    /**
     * Determines if all enemies in an encounter are dead, by running the isDead
     * method.
     * 
     * @param encounter the encounter to check.
     * @return true if they're all dead, else false.
     */
    public static boolean areAllEnemiesDead(EncounterInterface encounter) {
        for (EntityInterface enemy : encounter.getEnemies()) {
            if (!isDead(enemy)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if an entity is dead, by checking if their health is <= 0.
     * 
     * @param e the entity to check.
     * @return true if their health is <=0, else false.
     */
    public static boolean isDead(EntityInterface entity) {
        return entity.getHealth() <= 0;
    }

    /**
     * Determines if a run has been failed by the player running out of lives.
     * 
     * @param run the run to check.
     * @return true if the player has 0 or less lives, else false.
     */
    public static boolean isRunFailed(GameRunInterface run) {
        return run.getPlayer().getLives() <= 0;
    }

    /**
     * Determines if a run has been completed by reaching the final level.
     * 
     * @param run the run to check.
     * @return true if the run is on stage 11 or above (aka completed stage 10),
     *         else false.
     */
    public static boolean isRunComplete(GameRunInterface run) {
        return run.getStage() >= 11;
    }

    /**
     * Determine if a game run is over, either failed or completed.
     * 
     * @param run the run to check.
     * @return true if the run is failed or completed, else false.
     */
    public static boolean isRunOver(GameRunInterface run) {
        return isRunFailed(run) || isRunComplete(run);
    }

    /**
     * Uses fisher yates to shuffle an array. 
     * @param <T>
     * @param arr
     */
    public static <T> void shuffleArray(T[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
