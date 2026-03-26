package wizardquest.auth;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Authenticator - handles user auth. by calling the python-side auth module, parses the output, and returns AuthecationResult
 */
public class Authenticator implements AuthenticatorInterface {
    private static final String ISSUER = "https://accounts.google.com";
    private static final String SCOPES = "profile email";
    private static final String LOGINS_FILE = "../telemetry/logins_file.json";

    private final String clientId;
    private final String clientSecret;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Authenticator(){
        String id = System.getProperty("OIDC_CLIENT_ID", System.getenv("OIDC_CLIENT_ID"));
        String secret = System.getProperty("OIDC_CLIENT_SECRET", System.getenv("OIDC_CLIENT_SECRET"));
    }
    public static void main(String[] args) {
        Authenticator auth = new Authenticator();
        try {
            AuthenticationResult result = auth.login();
            System.out.println("Name: " + result.name());
            System.out.println("UserID: " + result.userID());
            System.out.println("Role: " + result.role());
        } catch (AuthenticationException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }
    @Override
    public AuthenticationResult login() throws AuthenticationException {

        validateEnvVars();

        JsonNode oauthConfig = fetchOAuthConfig();
        String authEndpoint = oauthConfig.get("authorization_endpoint").asText();
        String tokenEndpoint = oauthConfig.get("token_endpoint").asText();
        String userinfoEndpoint = oauthConfig.get("userinfo_endpoint").asText();

        CallbackHandler callbackHandler = new CallbackHandler();

        HttpServer server;

        try{
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            server.createContext("/callback", callbackHandler);
            server.start();

        } catch (IOException e) {
            throw new AuthenticationException("Failed to start the callback server", e);
        }

        int port = server.getAddress().getPort();
        String redirectUri = "http://127.0.0.1:" + port + "/callback";
        String[] pkce = makePkcePair();
        String codeVerifier = pkce[0];
        String codeChallenge = pkce[1];
        String state;
        // generate state
        SecureRandom random = new SecureRandom();
        byte[] stateBytes = new byte[24];
        random.nextBytes(stateBytes);
        state = Base64.getUrlEncoder().withoutPadding().encodeToString(stateBytes);

        String authURL = buildAuthUrl(authEndpoint, redirectUri, state, codeChallenge);
        openBrowser(authURL);

        String authCode;
        try{
            authCode = callbackHandler.waitForCode();
        }
        catch (InterruptedException e){
            throw new AuthenticationException("Login was interrupted" + e);
        }
        finally {
            server.stop(0);
        }
        if (authCode==null){
            throw new AuthenticationException("No auth code was received");
        }

        String accessToken = codeToToken(tokenEndpoint, authCode, redirectUri, codeVerifier);
        JsonNode userInfo = fetchUserInfo(userinfoEndpoint, accessToken);
        String name = userInfo.get("name").asText();
        String sub = userInfo.get("sub").asText();

        RoleEnum role = getRole(sub);
        return new AuthenticationResult(name, sub, role);
    }

    public void validateEnvVars() throws AuthenticationException{
        if (this.clientId.isEmpty()){
            throw new AuthenticationException("OIDC_CLIENT_ID env variable is not set");
        }
        if (this.clientSecret.isEmpty()){
            throw new AuthenticationException("OIDC_CLIENT_SECRET env variable is not set");
        }
    }

    public JsonNode fetchOAuthConfig() throws AuthenticationException {

        try{
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ISSUER + "/.well-known/openid-configuration")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() !=200){
                throw new AuthenticationException("Failed to fetch the o auth configs: HTTP " + response.statusCode());
            }
            return mapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("Failed to fetch the OAuth configuration" + e);
        }


    }
    public String[] makePkcePair(){
        SecureRandom random = new SecureRandom();
        byte[] verifierBytes = new byte[32];

        random.nextBytes(verifierBytes);
        String codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(verifierBytes);

        try {
            byte[] challengeHash = MessageDigest.getInstance("SHA-256").digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(challengeHash);
            return new String[] {codeVerifier, codeChallenge};
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static String encode(String val){
        return URLEncoder.encode(val, StandardCharsets.UTF_8);
    }
    public String buildAuthUrl(String authEndpoint, String redirectUri, String state, String codeChallenge){
        return authEndpoint + "?" + String.join("&",
                "response_type=code",
                "client_id=" + encode(clientId),
                "redirect_uri="+encode(redirectUri),
                "scope="+encode(SCOPES),
                "state="+encode(state),
                "code_challenge="+encode(codeChallenge),
                "code_challenge_method=S256"
        );
    }

    public void openBrowser(String url) throws AuthenticationException{
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            throw new AuthenticationException("Failed to open the browser ",e);
        }

    }

    public String codeToToken(String tokenEndpoint, String code, String redirectUri, String codeVerifier){
        String body = String.join("&",
                "grant_type=authorization_code",
                "code=" + encode(code),
                "redirect_uri="+encode(redirectUri),
                "client_id="+encode(clientId),
                "client_secret="+encode(clientSecret),
                "code_verifier="+encode(codeVerifier)
        );

        HttpRequest request = (HttpRequest) HttpRequest
                .newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Content-type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()!=200){
                throw new AuthenticationException("Token exchange failure: HTTP " + response.statusCode() + response.body());
            }
            JsonNode tokens = mapper.readTree(response.body());
            return tokens.get("access_token").asText();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

    }

    public JsonNode fetchUserInfo(String userinfoEndpoint, String accessToken) throws AuthenticationException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(userinfoEndpoint)).header("Authorization", "Bearer " + accessToken).GET().build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() != 200){
            throw new AuthenticationException("Failed to fetch user info, HTTP" + response.statusCode());
        }
        try {
            return mapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public RoleEnum getRole(String userID) throws AuthenticationException{
        File file = new File(LOGINS_FILE);

        try{
            ObjectNode roles;
            if (file.exists()){
                roles = (ObjectNode) mapper.readTree(file);
            }
            else{
                roles = mapper.createObjectNode();
            }
            JsonNode roleNode = roles.get(userID);
            if (roleNode == null){ // default case is player
                roles.put(userID, "player");
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, roles);
                return RoleEnum.PLAYER;
            }
            return RoleEnum.valueOf(roleNode.asText().toUpperCase());
        } catch (IOException e) {
            throw new AuthenticationException("Failed to read and write logins file", e);
        }
    }

    private static class CallbackHandler implements HttpHandler{
        private final CompletableFuture<String> codeFuture = new CompletableFuture<>();
        private volatile String receivedState;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);

            this.receivedState = params.get("state");
            String code = params.get("code");
            String error = params.get("error");

            String html = "<html><body><h3>Login complete. "
                    + "You can now close this tab.</h3></body></html>"
                    + "<style>body {"
                    + "font-family: Arial, sans-serif; "
                    + "display: flex; "
                    + "justify-content: center; "
                    + "align-items: center; "
                    + "height: 100vh; "
                    + "background: #2c2f33"
                    + "} "
                    + "h3 {"
                    + "color: #9b59ff; "
                    + "font-size: 36px; "
                    + "font-weight: bold; "
                    + "align-items: center; "
                    + "justify-content: center;"
                    + "}</style>";

            byte[] responseBytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);

            if (error!=null){
                codeFuture.completeExceptionally(new AuthenticationException("OAuth error: " + error));
            }
            else{
                codeFuture.complete(code);
            }
        }

        public String waitForCode() throws AuthenticationException, InterruptedException {
            try{
                return codeFuture.get();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof AuthenticationException ae){
                    throw ae;
                }
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private Map<String, String> parseQuery(String query) {
            if (query == null || query.isEmpty()){
                return Map.of();
            }
            String[] params = query.split("&");
            HashMap<String, String> m = new HashMap<>();

            for (String param: params){
                String[] parts = param.split("=", 2);
                if (parts.length==2){
                    String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                    m.put(key, value);
                }
            }
            return m;
        }
    }
}