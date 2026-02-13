import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class auth {

    private static final String ISSUER = "https://accounts.google.com";
    private static final String CLIENT_ID = System.getenv("OIDC_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("OIDC_CLIENT_SECRET");

    private static final List<String> SCOPES = List.of("profile", "email");

    private static final Logger LOG = Logger.getLogger(auth.class.getName());

    static {
        try {
            Files.createDirectories(Path.of("auth"));
            Handler fh = new FileHandler("auth/logs.txt", true);
            fh.setLevel(Level.INFO);

            fh.setFormatter(new java.util.logging.Formatter() {
                private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                @Override
                public String format(LogRecord record) {
                    String ts = LocalDateTime.now().format(dtf);
                    return ts + "\n    |" + record.getMessage() + "\n";
                }
            });

            LOG.setUseParentHandlers(false);
            LOG.addHandler(fh);
            LOG.setLevel(Level.INFO);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set up logging to auth/logs.txt", e);
        }
    }

    public static final class User {
        public final String sub;
        public final String name;

        public User(String sub, String name) {
            this.sub = sub;
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{sub='" + sub + "', name='" + name + "'}";
        }
    }

    private static void validateEnv() {
        if (ISSUER == null || ISSUER.isBlank()) {
            throw new IllegalStateException("Issuer is missing.");
        }
        if (CLIENT_ID == null || CLIENT_ID.isBlank()) {
            throw new IllegalStateException("OIDC_CLIENT_ID env var is not set.");
        }
        if (CLIENT_SECRET == null || CLIENT_SECRET.isBlank()) {
            throw new IllegalStateException("OIDC_CLIENT_SECRET env var is not set.");
        }
    }

    private static String b64Url(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static String randomUrlSafe(int bytes) {
        byte[] b = new byte[bytes];
        new SecureRandom().nextBytes(b);
        return b64Url(b);
    }

    private static final class Pkce {
        final String verifier;
        final String challenge;

        Pkce(String verifier, String challenge) {
            this.verifier = verifier;
            this.challenge = challenge;
        }
    }

    private static Pkce makePkce() {
        try {
            String verifier = randomUrlSafe(32);
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha256.digest(verifier.getBytes(StandardCharsets.US_ASCII));
            String challenge = b64Url(digest);
            return new Pkce(verifier, challenge);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PKCE pair", e);
        }
    }

    private static void openBrowser(String url) {
        try {
            if (!Desktop.isDesktopSupported()) {
                throw new RuntimeException("Desktop browsing not supported on this machine.");
            }
            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception e) {
            throw new RuntimeException("Failed to open browser: " + e.getMessage(), e);
        }
    }

    private static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> out = new HashMap<>();
        if (rawQuery == null || rawQuery.isBlank()) return out;

        for (String part : rawQuery.split("&")) {
            int eq = part.indexOf('=');
            String k = (eq >= 0) ? part.substring(0, eq) : part;
            String v = (eq >= 0) ? part.substring(eq + 1) : "";
            out.put(
                    URLDecoder.decode(k, StandardCharsets.UTF_8),
                    URLDecoder.decode(v, StandardCharsets.UTF_8)
            );
        }
        return out;
    }

    private static final class CallbackData {
        volatile String code;
        volatile String state;
        volatile String error;
        volatile String errorDescription;
    }

    private static final class CallbackServer {
        final HttpServer server;
        final String redirectUri;
        final CallbackData data;
        final CountDownLatch latch;

        CallbackServer(HttpServer server, String redirectUri, CallbackData data, CountDownLatch latch) {
            this.server = server;
            this.redirectUri = redirectUri;
            this.data = data;
            this.latch = latch;
        }

        void stop() {
            server.stop(0);
        }
    }

    private static CallbackServer startCallbackServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            CallbackData data = new CallbackData();
            CountDownLatch latch = new CountDownLatch(1);

            server.createContext("/callback", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    try {
                        URI uri = exchange.getRequestURI();
                        Map<String, String> q = parseQuery(uri.getRawQuery());

                        data.code = q.get("code");
                        data.state = q.get("state");
                        data.error = q.get("error");
                        data.errorDescription = q.get("error_description");

                        String html = ""
                                + "<html><body><h3>Login complete. You can now close this tab.</h3></body></html>"
                                + "<style>"
                                + "body{font-family:Arial,sans-serif;display:flex;justify-content:center;align-items:center;"
                                + "height:100vh;background:#2c2f33}"
                                + "h3{color:#9b59ff;font-size:36px;font-weight:bold;}"
                                + "</style>";

                        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
                        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
                        exchange.sendResponseHeaders(200, bytes.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(bytes);
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });

            server.createContext("/", exchange -> {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
            });

            server.start();
            int port = server.getAddress().getPort();
            String redirectUri = "http://127.0.0.1:" + port + "/callback";

            return new CallbackServer(server, redirectUri, data, latch);
        } catch (IOException e) {
            throw new RuntimeException("Failed to start callback server", e);
        }
    }

    private static String formEncode(Map<String, String> fields) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> e : fields.entrySet()) {
            if (!first) sb.append("&");
            first = false;
            sb.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8));
            sb.append("=");
            sb.append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    private static String httpGet(HttpClient client, String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                throw new RuntimeException("GET failed: " + resp.statusCode() + " body=" + resp.body());
            }
            return resp.body();
        } catch (Exception e) {
            throw new RuntimeException("GET error: " + e.getMessage(), e);
        }
    }

    private static String httpPostForm(HttpClient client, String url, Map<String, String> form) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(formEncode(form)))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                throw new RuntimeException("POST failed: " + resp.statusCode() + " body=" + resp.body());
            }
            return resp.body();
        } catch (Exception e) {
            throw new RuntimeException("POST error: " + e.getMessage(), e);
        }
    }

    private static String jsonGetString(String json, String key) {
        String needle = "\"" + key + "\"";
        int i = json.indexOf(needle);
        if (i < 0) return null;

        int colon = json.indexOf(':', i + needle.length());
        if (colon < 0) return null;

        int j = colon + 1;
        while (j < json.length() && Character.isWhitespace(json.charAt(j))) j++;
        if (j >= json.length() || json.charAt(j) != '"') return null;

        j++;
        StringBuilder out = new StringBuilder();
        boolean esc = false;

        while (j < json.length()) {
            char c = json.charAt(j++);
            if (esc) {
                out.append(c);
                esc = false;
                continue;
            }
            if (c == '\\') {
                esc = true;
                continue;
            }
            if (c == '"') break;
            out.append(c);
        }
        return out.toString();
    }

    private static final class OidcConfig {
        final String authorizationEndpoint;
        final String tokenEndpoint;
        final String userinfoEndpoint;

        OidcConfig(String authorizationEndpoint, String tokenEndpoint, String userinfoEndpoint) {
            this.authorizationEndpoint = authorizationEndpoint;
            this.tokenEndpoint = tokenEndpoint;
            this.userinfoEndpoint = userinfoEndpoint;
        }
    }

    private static OidcConfig fetchOidcConfig(HttpClient client) {
        String json = httpGet(client, ISSUER + "/.well-known/openid-configuration");

        String authEndpoint = jsonGetString(json, "authorization_endpoint");
        String tokenEndpoint = jsonGetString(json, "token_endpoint");
        String userinfoEndpoint = jsonGetString(json, "userinfo_endpoint");

        if (authEndpoint == null || tokenEndpoint == null || userinfoEndpoint == null) {
            throw new RuntimeException("Could not parse OIDC config: " + json);
        }
        return new OidcConfig(authEndpoint, tokenEndpoint, userinfoEndpoint);
    }

    public static User googleLogin() {
        validateEnv();

        HttpClient client = HttpClient.newHttpClient();
        OidcConfig cfg = fetchOidcConfig(client);

        CallbackServer cb = startCallbackServer();
        String redirectUri = cb.redirectUri;

        String state = randomUrlSafe(24);
        Pkce pkce = makePkce();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("response_type", "code");
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", redirectUri);
        params.put("scope", String.join(" ", SCOPES));
        params.put("state", state);
        params.put("code_challenge", pkce.challenge);
        params.put("code_challenge_method", "S256");

        String authUrl = cfg.authorizationEndpoint + "?" + formEncode(params);

        openBrowser(authUrl);
        LOG.info("SIE: Sign-in prompted " + LocalDateTime.now());

        try {
            cb.latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            cb.stop();
            throw new RuntimeException("Interrupted while waiting for callback");
        } finally {
            cb.stop();
        }

        if (cb.data.error != null && !cb.data.error.isBlank()) {
            LOG.warning("SIE: Sign-in failed: " + cb.data.error + " " + cb.data.errorDescription);
            throw new RuntimeException("Auth error: " + cb.data.error + " " + cb.data.errorDescription);
        }

        if (cb.data.code == null || cb.data.code.isBlank()) {
            LOG.warning("SIE: Sign-in failed: no authorisation code received.");
            throw new RuntimeException("No authorisation code received.");
        }

        if (!Objects.equals(cb.data.state, state)) {
            LOG.warning("SIE: Sign-in failed: state mismatch.");
            throw new RuntimeException("AUTH ERROR: State mismatch.");
        }

        Map<String, String> tokenForm = new LinkedHashMap<>();
        tokenForm.put("grant_type", "authorization_code");
        tokenForm.put("code", cb.data.code);
        tokenForm.put("redirect_uri", redirectUri);
        tokenForm.put("client_id", CLIENT_ID);
        tokenForm.put("client_secret", CLIENT_SECRET);
        tokenForm.put("code_verifier", pkce.verifier);

        String tokenJson;
        try {
            tokenJson = httpPostForm(client, cfg.tokenEndpoint, tokenForm);
        } catch (RuntimeException ex) {
            System.out.println("---- AUTH ERROR OCCURRED ----");
            System.out.println("|  Token exchange failed");
            throw ex;
        }

        String accessToken = jsonGetString(tokenJson, "access_token");
        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("No access_token in token response: " + tokenJson);
        }

        String userinfoJson;
        try {
            HttpRequest userinfoReq = HttpRequest.newBuilder(URI.create(cfg.userinfoEndpoint))
                    .GET()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> resp = client.send(userinfoReq, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                throw new RuntimeException("userinfo failed: " + resp.statusCode() + " body=" + resp.body());
            }
            userinfoJson = resp.body();
        } catch (Exception e) {
            throw new RuntimeException("userinfo request error: " + e.getMessage(), e);
        }

        String sub = jsonGetString(userinfoJson, "sub");
        String name = jsonGetString(userinfoJson, "name");

        LOG.info("Sign-in successful: user " + (name == null ? "(unknown)" : name) + " authenticated.");
        return new User(sub, name);
    }

    public static void main(String[] args) {
        System.out.println(googleLogin());
    }

    private auth() {}
}
