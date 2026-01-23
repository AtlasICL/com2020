package WizardQuest;

import java.time.LocalDateTime;
/**
 * Provides a singleton global access point to the time manager
 */
public class TimeManagerSingleton {
    private static TimeManagerInterface timeManager = new TimeManager();

    private TimeManagerSingleton() {
    }

    /**
     * Returns a reference to the time manager.
     * @return the time manager. 
     */
    public static TimeManagerInterface getTimeManager() {
        return timeManager;
    }

    private static class TimeManager implements TimeManagerInterface {
        public TimeManager() {
        }

        /**
         * Gives the current time.
         * 
         * @return the current time. 
         */
        @Override
        public LocalDateTime getCurrentTime() {
            return LocalDateTime.now();
        }
    }
}
