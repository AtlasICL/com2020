package WizardQuest;

import java.io.File;
import java.lang.ProcessBuilder;

public class Authenticator implements AuthenticatorInterface {

    @Override
    public AuthenticationResult login() {
        
        String name = "";
        String userID = "";
        RoleEnum role = RoleEnum.PLAYER;

        // Instantiate the process builder, and set it up to be able
        // to run the login python script.
        ProcessBuilder procBuilder = new ProcessBuilder("python");
        procBuilder.directory(new File("../telemetry/auth/"));

        // We only want stdout not stderror
        procBuilder.redirectErrorStream(false);

        procBuilder.start();

        // Parse the output ...

        return new AuthenticationResult(
            name, 
            userID,
            role
        );
    }

}
