package WizardQuest;

import java.io.File;

public interface FileIOInterface {
    /**
     * Sets the destination file the class reads from and writes to.
     * 
     * @param file the file the class should work with
     */
    public void setDestinationFile(File file);

    /**
     * Resets the destination file to the default location
     */
    public void resetDestinationFile();
}
