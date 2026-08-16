import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DevServer — serves output/ over HTTP and exposes ADD / DELETE endpoints that
 * re-write the `products = [...]` block in app.py, regenerate the whole site via
 * CompilerPipeline, then 303-redirect back to /products.html.
 *
 *   GET  /products.html          → static file from output/
 *   POST /api/add                → body: name, price, details  (form-urlencoded)
 *   GET  /api/delete?id=N        → remove product with that id
 *
 * Requires Java 17+ (com.sun.net.httpserver).
 */
public class DevServer {
    private static final int PORT = 8080;

    // Matches one product dict inside the products = [...] block:
    //     {"id": 1, "name": "Widget", "price": 25, "details": "..."}
    private static final Pattern PRODUCT_ENTRY = Pattern.compile(
        "\\{\\s*\"id\"\\s*:\\s*(\\d+)\\s*,\\s*" +
        "\"name\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"\\s*,\\s*" +
        "\"price\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)\\s*,\\s*" +
        "\"details\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"\\s*\\}");

    private final String projectDir;
    private final HttpServer server;

    public DevServer(String projectDir) throws IOException {
        this.projectDir = projectDir;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/api/add", new AddHandler());
        server.createContext("/api/delete", new DeleteHandler());
        server.createContext("/", new FileHandler());
    }

    /** Start serving and block forever (until the JVM is killed). */
    public void start() {
        server.start();
        System.out.println("[server] listening on http://localhost:" + PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ─── Handlers ──────────────────────────────────────────────

    private final class AddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            Map<String, String> params = parseUrlEncoded(exchange.getRequestBody().readAllBytes());
            String name    = params.getOrDefault("name", "").trim();
            String details = params.getOrDefault("details", "").trim();
            String priceStr = params.getOrDefault("price", "").trim();
            System.out.println("[server] POST /api/add  name=" + name + "  price=" + priceStr + "  details=" + details);
            if (name.isEmpty() || priceStr.isEmpty()) {
                System.out.println("[server] -> 400 Bad Request (missing name/price)");
                exchange.sendResponseHeaders(400, -1);
                exchange.close();
                return;
            }

            List<Map<String, String>> products = readProducts();
            int nextId = 1;
            for (Map<String, String> p : products) {
                int id = Integer.parseInt(p.get("id"));
                if (id >= nextId) { nextId = id + 1; }
            }

            Map<String, String> entry = new LinkedHashMap<>();
            entry.put("id", String.valueOf(nextId));
            entry.put("name", name);
            entry.put("price", normalizePrice(priceStr));
            entry.put("details", details);
            products.add(entry);

            System.out.println("[server] -> new id=" + nextId);
            rewriteProducts(products);
            regenerate();
            System.out.println("[server] -> done, redirecting to /products.html");

            redirect(exchange, "/products.html");
        }
    }

    private final class DeleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            String idStr = query == null ? null : URLDecoder.decode(valueOf(query, "id"), StandardCharsets.UTF_8);
            if (idStr == null || idStr.isEmpty()) {
                System.out.println("[server] GET /api/delete -> 400 Bad Request (missing id)");
                exchange.sendResponseHeaders(400, -1);
                exchange.close();
                return;
            }

            System.out.println("[server] GET /api/delete?id=" + idStr);

            List<Map<String, String>> products = readProducts();
            boolean removed = products.removeIf(p -> p.get("id").equals(idStr));
            System.out.println("[server] -> " + (removed ? "removed" : "id not found, nothing removed"));

            rewriteProducts(products);
            regenerate();
            System.out.println("[server] -> done, redirecting to /products.html");

            redirect(exchange, "/products.html");
        }
    }

    private final class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String rawPath = exchange.getRequestURI().getPath();
            if (rawPath == null || rawPath.contains("..")) {
                System.out.println("[server] GET " + rawPath + " -> 400 Bad Request");
                sendCode(exchange, 400);
                return;
            }
            String rel = rawPath.replace('\\', '/');
            while (rel.startsWith("/")) { rel = rel.substring(1); }
            if (rel.isEmpty()) { rel = "index.html"; }

            Path file = Paths.get(projectDir, "output", rel);
            if (!Files.isRegularFile(file)) {
                System.out.println("[server] GET /" + rel + " -> 404 Not Found");
                sendCode(exchange, 404);
                return;
            }

            byte[] body = Files.readAllBytes(file);
            exchange.getResponseHeaders().set("Content-Type", contentType(rel));
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
            System.out.println("[server] GET /" + rel + " -> 200 OK (" + body.length + " bytes)");
        }
    }

    // ─── app.py read / rewrite ─────────────────────────────────

    /** Parse the products list out of app.py into ordered maps. */
    private List<Map<String, String>> readProducts() throws IOException {
        String source = Files.readString(appPy(), StandardCharsets.UTF_8);
        int blockStart = source.indexOf("products = [");
        int blockEnd;
        if (blockStart < 0) {
            throw new IOException("app.py is missing the 'products = [' list");
        }
        blockStart += "products = [".length();
        blockEnd = source.indexOf(']', blockStart);
        if (blockEnd < 0) {
            throw new IOException("app.py products list is not closed with ']'");
        }

        String block = source.substring(blockStart, blockEnd);
        List<Map<String, String>> products = new ArrayList<>();
        Matcher m = PRODUCT_ENTRY.matcher(block);
        while (m.find()) {
            Map<String, String> entry = new LinkedHashMap<>();
            entry.put("id", m.group(1));
            entry.put("name", unescape(m.group(2)));
            entry.put("price", m.group(3));
            entry.put("details", unescape(m.group(4)));
            products.add(entry);
        }
        return products;
    }

    /** Rewrite the products = [...] block in app.py, preserving everything else. */
    private void rewriteProducts(List<Map<String, String>> products) throws IOException {
        String source = Files.readString(appPy(), StandardCharsets.UTF_8);
        int blockStart = source.indexOf("products = [");
        int blockEnd = source.indexOf(']', blockStart + "products = [".length());

        StringBuilder block = new StringBuilder("products = [");
        for (int i = 0; i < products.size(); i++) {
            Map<String, String> p = products.get(i);
            block.append("\n    {\"id\": ").append(p.get("id"))
                 .append(", \"name\": \"").append(escape(p.get("name")))
                 .append("\", \"price\": ").append(p.get("price"))
                 .append(", \"details\": \"").append(escape(p.get("details")))
                 .append("\"}");
            if (i < products.size() - 1) { block.append(","); }
        }
        block.append("\n]");

        String rewritten = source.substring(0, blockStart) + block + source.substring(blockEnd + 1);
        Files.writeString(appPy(), rewritten, StandardCharsets.UTF_8);
    }

    /** Re-run the static-site pipeline so output/ reflects the new app.py. */
    private void regenerate() {
        System.out.println("[server] ===== REGENERATING site (CompilerPipeline) =====");
        long start = System.currentTimeMillis();
        new CompilerPipeline(projectDir).run();
        System.out.println("[server] ===== Regeneration finished in "
                + (System.currentTimeMillis() - start) + " ms =====");
    }

    private Path appPy() {
        return Paths.get(projectDir, "app.py");
    }

    // ─── small helpers ─────────────────────────────────────────

    private static String valueOf(String query, String key) {
        for (String pair : query.split("[&;]")) {
            int eq = pair.indexOf('=');
            String k = eq < 0 ? pair : pair.substring(0, eq);
            String v = eq < 0 ? "" : pair.substring(eq + 1);
            if (key.equals(k)) { return v; }
        }
        return "";
    }

    private static Map<String, String> parseUrlEncoded(byte[] raw) {
        Map<String, String> map = new LinkedHashMap<>();
        String body = new String(raw, StandardCharsets.UTF_8);
        for (String pair : body.split("[&;]")) {
            if (pair.isEmpty()) { continue; }
            int eq = pair.indexOf('=');
            String k = eq < 0 ? pair : pair.substring(0, eq);
            String v = eq < 0 ? "" : pair.substring(eq + 1);
            map.put(URLDecoder.decode(k, StandardCharsets.UTF_8),
                    URLDecoder.decode(v, StandardCharsets.UTF_8));
        }
        return map;
    }

    /** Keep numeric price as-is (a bare Python literal, never quoted). */
    private static String normalizePrice(String raw) {
        String s = raw.trim();
        if (s.isEmpty()) { return "0"; }
        try {
            long asLong = Long.parseLong(s);
            return String.valueOf(asLong);
        } catch (NumberFormatException ignored) {
            // fall through to double
        }
        try {
            double d = Double.parseDouble(s);
            if (!Double.isFinite(d)) { return "0"; }
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        } catch (NumberFormatException ignored) {
            return "0";
        }
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescape(String value) {
        StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                char n = value.charAt(++i);
                if (n == 'n') { sb.append('\n'); }
                else if (n == 't') { sb.append('\t'); }
                else if (n == 'r') { sb.append('\r'); }
                else { sb.append(n); }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String contentType(String rel) {
        String lower = rel.toLowerCase();
        if (lower.endsWith(".html")) { return "text/html; charset=utf-8"; }
        if (lower.endsWith(".css"))  { return "text/css; charset=utf-8"; }
        if (lower.endsWith(".js"))   { return "application/javascript; charset=utf-8"; }
        if (lower.endsWith(".png"))  { return "image/png"; }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) { return "image/jpeg"; }
        if (lower.endsWith(".svg"))  { return "image/svg+xml"; }
        if (lower.endsWith(".ico"))  { return "image/x-icon"; }
        return "application/octet-stream";
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(303, -1);
        exchange.close();
    }

    private static void sendCode(HttpExchange exchange, int code) throws IOException {
        exchange.sendResponseHeaders(code, -1);
        exchange.close();
    }
}