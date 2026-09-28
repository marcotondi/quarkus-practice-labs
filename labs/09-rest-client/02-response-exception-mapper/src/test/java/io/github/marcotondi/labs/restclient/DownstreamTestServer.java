package io.github.marcotondi.labs.restclient;

import com.sun.net.httpserver.HttpServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Server HTTP in-process: risponde 200 per id=1, 404 per gli altri.
 * NON modificare.
 */
public class DownstreamTestServer implements QuarkusTestResourceLifecycleManager {

    private HttpServer server;
    private int port;

    @Override
    public Map<String, String> start() {
        try {
            server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/api/widgets", exchange -> {
                String path = exchange.getRequestURI().getPath();
                String id = path.substring(path.lastIndexOf('/') + 1);
                if ("1".equals(id)) {
                    respond(exchange, 200, "{\"id\":1,\"name\":\"Gadget\"}");
                } else {
                    respond(exchange, 404, "{\"error\":\"not found\"}");
                }
            });
            server.start();
            port = server.getAddress().getPort();
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile avviare il server di test", e);
        }
        return Map.of("quarkus.rest-client.widgets-api.url", "http://localhost:" + port);
    }

    private void respond(com.sun.net.httpserver.HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}