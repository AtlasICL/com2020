package WizardQuest;

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
        RoleEnum role = RoleEnum.PLAYER; // default

        ProcessBuilder procBuilder = new ProcessBuilder(
            "python",
            "-m",
            "auth.auth_wrapper"
        );

        procBuilder.directory(new File("../telemetry"));

        procBuilder.redirectErrorStream(false);

        try {
            Process proc = procBuilder.start();

            String output;

            try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(proc.getInputStream()))) {

                output = reader.lines().collect(Collectors.joining());
            }
            catch (IOException e) {
                throw new AuthenticationException("Auth error: " + e.getMessage());
            }

            proc.waitFor();

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(output);

                userID = json.get("sub") != null
                        ? json.get("sub").asText()
                        : "";

                name = json.get("name") != null
                        ? json.get("name").asText()
                        : "";

                JsonNode roleNode = json.get("role");

                if (roleNode != null) {
                    String roleVal = roleNode.asText();

                    if (roleVal != null && !roleVal.isBlank()) {
                        try {
                            role = RoleEnum.valueOf(
                                    roleVal.trim().toUpperCase()
                            );
                        }
                        catch (IllegalArgumentException ignored) {
                            // If invalid role -> stay PLAYER
                        }
                    }
                }

            }
            catch (JsonProcessingException e) {
                throw new AuthenticationException(
                        "Auth error while parsing Python output: " + e.getMessage()
                );
            }

        }
        catch (InterruptedException e) {
            throw new AuthenticationException(
                    "Auth error waiting for Python: " + e.getMessage()
            );
        }
        catch (IOException e) {
            throw new AuthenticationException(
                    "Auth error running Python: " + e.getMessage()
            );
        }

        return new AuthenticationResult(
            name,
            userID,
            role
        );
    }

    //FOR TESTING
    public static void main(String[] args) {

        Authenticator auth = new Authenticator();

        try {
            AuthenticationResult result = auth.login();

            System.out.println("Name: " + result.name());
            System.out.println("UserID: " + result.userID());
            System.out.println("Role: " + result.role());

        }
        catch (AuthenticationException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }
}

