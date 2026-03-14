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
        private Instant currentTime;
        private final Random random;
        public SimulationTimeManager() {
            currentTime = Instant.now();
            random = new Random();
        }

        @Override
        public Instant getCurrentTime() {
            return Instant.now();
        }
    }
}
