package wizardquest.unit.gamemanager;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import wizardquest.gamemanager.TimeManagerInterface;
import wizardquest.gamemanager.TimeManagerSingleton;

public class TimeManagerUnitTests {

    private TimeManagerInterface timeManager;

    /**
     * Using actual time in the time manager should return the current time.
     */
    @Test
    void getCurrentTime_currentForActualTime() {
        // Set the time manager to use actual time.
        TimeManagerSingleton.useActualTime();
        timeManager = TimeManagerSingleton.getInstance();
        // Comparing the exact values of Instant.now() and the time manager's view of the current
        // time will likely result in miniscule differences that could cause this test to fail.
        // Therefore, we have implemented a small threshold of 1000ms.
        final int MS_THRESHOLD = 1000;
        Instant expectedTime = Instant.now();
        Instant actualTime = timeManager.getCurrentTime();
        long msDifference = Math.abs(Duration.between(expectedTime, actualTime).toMillis());
        assertTrue(msDifference < MS_THRESHOLD);
    }

    /**
     * Using simulated time in the time manager should return a time in the past.
     * Every time the method is invoked, a time later than the last should be
     * returned, with a small controlled jump in time.
     */
    @Test
    void getCurrentTime_sequentialForSimulationTime() {
        // Set the time manager to use simulation time.
        TimeManagerSingleton.useSimulationTime();
        timeManager = TimeManagerSingleton.getInstance();
        assertTrue(timeManager.getCurrentTime().isBefore(Instant.now()));
        // Collect ten timestamps one after the other.
        // These should all be sequential.
        Instant[] times = new Instant[10];
        for (int i = 0; i < 10; i++) {
            times[i] = timeManager.getCurrentTime();
            if (i > 0) {
                assertTrue(times[i].isAfter(times[i - 1]));
            }
        }
    }
}
