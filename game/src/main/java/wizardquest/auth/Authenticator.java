package wizardquest.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
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
                "python3",
                "-m",
                "auth.auth_wrapper");
        procBuilder.directory(new File("../telemetry"));

        // We only want stdout not stderror
        procBuilder.redirectErrorStream(false);

        try {
            Process proc = procBuilder.start();

            String output;
            String errorOutput;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    BufferedReader errReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                output = reader.lines().collect(Collectors.joining());
                errorOutput = errReader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                throw new AuthenticationException("Auth error." + e.getMessage());
            }

            int exitCode = proc.waitFor();

            if (exitCode != 0) {
                throw new AuthenticationException(
                        "Python auth module exited with code " + exitCode + ": " + errorOutput);
            }

            if (output.isEmpty()) {
                throw new AuthenticationException(
                        "Python auth module returned no output. Errors: " + errorOutput);
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(output);

                if (json == null || json.isNull() || json.isMissingNode()) {
                    throw new AuthenticationException(
                            "Python auth module returned invalid JSON: " + output);
                }

                userID = json.get("sub") != null ? json.get("sub").asText() : "";
                name = json.get("name") != null ? json.get("name").asText() : "";
                JsonNode roleNode = json.get("role");
                if (roleNode == null) {
                    throw new AuthenticationException(
                            "Error parsing authenticated user's role. Output: " + output);
                }
                String roleVal = roleNode.asText();
                role = RoleEnum.valueOf(roleVal.toUpperCase());

            } catch (JsonProcessingException e) {
                throw new AuthenticationException(
                        "Auth error while parsing python login module output: " + output);
            }

        } catch (InterruptedException e) {
            throw new AuthenticationException(
                    "Auth error while waiting for Python auth module response" + e.getMessage());
        } catch (IOException e) {
            throw new AuthenticationException("Auth error occurred from Python process" + e.getMessage());
        }

        return new AuthenticationResult(
                name,
                userID,
                role);
    }

    // // FOR TESTING
    // // To test, use mvn -f C:\eaca\com2020\game\pom.xml compile exec:java
    // "-Dexec.mainClass=WizardQuest.Authenticator"
    // public static void main(String[] args) {
    // Authenticator auth = new Authenticator();
    // try {
    // AuthenticationResult result = auth.login();
    // System.out.println("Name: " + result.name());
    // System.out.println("UserID: " + result.userID());
    // System.out.println("Role: " + result.role());
    // } catch (AuthenticationException e) {
    // System.err.println("Login failed: " + e.getMessage());
    // }
    // }

}
