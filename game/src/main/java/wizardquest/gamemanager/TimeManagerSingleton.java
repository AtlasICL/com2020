package wizardquest.gamemanager;

import java.time.Instant;
import java.util.Random;

/**
 * Provides a singleton global access point to the time manager
 */
public class TimeManagerSingleton {
    private static TimeManagerInterface timeManager = new TimeManager();

    private TimeManagerSingleton() {
    }

    /**
     * Returns a reference to the time manager.
     * 
     * @return the time manager.
     */
    public static TimeManagerInterface getInstance() {
        return timeManager;
    }

    public static void useSimulationTime(){
        timeManager = new SimulationTimeManager();
    }

    public static void useActualTime(){
        timeManager = new TimeManager();
    }

    private static class TimeManager implements TimeManagerInterface {
        public TimeManager() {
        }

        @Override
        public Instant getCurrentTime() {
            return Instant.now();
        }
    }

    private static class SimulationTimeManager implements TimeManagerInterface {
        private static Instant currentTime = null;
        private static final Random random = new Random();

        public SimulationTimeManager() {
            if (currentTime == null) {
                // Start the sessions at a past date (in this case, 3 weeks ago).
                // We do this because telemetry events will be marked invalid if they
                // are detected as being in the future (part of our data validation).
                long THREE_WEEKS = 86400 * 21;
                currentTime = Instant.now().minusSeconds(THREE_WEEKS);
            }
        }

        @Override
        public Instant getCurrentTime() {
            // Advance the time by a random amount.
            // TODO: we could advance time by an amount which scales with difficulty.
            // This would make it more realistic.
            currentTime = currentTime.plusSeconds(random.nextInt(10) + 1);
            return currentTime;
        }
    }
}
