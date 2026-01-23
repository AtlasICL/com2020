package WizardQuest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public LocalDateTime getCurrentTime();

    /**
     * Converts a LocalDateTime to format: YYYY/MM/DD/HH/MM/SS
     * @param time the time to convert.
     * @return the converted time. 
     */
    public static String convertDateTime(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss");
        return time.format(formatter);
    }
}
