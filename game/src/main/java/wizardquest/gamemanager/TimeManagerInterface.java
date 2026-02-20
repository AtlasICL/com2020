package wizardquest.gamemanager;

import java.time.Instant;

/**
 * Interface the time manager implements. Acts as a global access point to time
 * information to allow testing of time sensitive functions.
 */
public interface TimeManagerInterface {
    /**
     * Provides access to the current time when the method is called.
     * 
     * @return the time when the method is called.
     */
    public Instant getCurrentTime();
}
