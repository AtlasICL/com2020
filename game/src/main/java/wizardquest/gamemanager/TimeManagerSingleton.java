package wizardquest.gamemanager;

import java.time.Instant;

/**
 * Provides a singleton global access point to the time manager
 */
public class TimeManagerSingleton {
    private static final TimeManagerInterface timeManager = new TimeManager();

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

    private static class TimeManager implements TimeManagerInterface {
        public TimeManager() {
        }

        @Override
        public Instant getCurrentTime() {
            return Instant.now();
        }
    }
}
