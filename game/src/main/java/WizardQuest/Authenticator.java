package WizardQuest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Authenticator implements AuthenticatorInterface {

    @Override
    public AuthenticationResult login() throws AuthenticationException {
        
        String name = "";
        String userID = "";
        RoleEnum role = RoleEnum.PLAYER;

        // Instantiate the process builder, and set it up to be able
        // to run the login python script.
        ProcessBuilder procBuilder = new ProcessBuilder(
            "python",
            "-m",
            "auth.auth_wrapper"
        );
        procBuilder.directory(new File("../telemetry"));

        // We only want stdout not stderror
        procBuilder.redirectErrorStream(false);

        try {
            Process proc = procBuilder.start();

            String output;
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining());
            } catch (IOException e) {
                throw new AuthenticationException("Auth error." + e);
            }

            proc.waitFor();

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(output);
                name = json.get("name").asText();
                userID = json.get("sub").asText();
                String roleVal = json.get("role").asText();
                role = RoleEnum.valueOf(roleVal.toUpperCase());
            } catch (JsonProcessingException e) {
                throw new AuthenticationException("Auth error while parsing python login module output" + e); 
            }

        } catch (InterruptedException e) {
            throw new AuthenticationException("Auth error while waiting for Python auth module response" + e);
        } catch (IOException e) {
            throw new AuthenticationException("Auth error occurred from Python process" + e);
        }

        return new AuthenticationResult(
            name, 
            userID,
            role
        );
    }

}
