package io.github.marcotondi.labs.restclient;

import com.sun.net.httpserver.HttpServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Server HTTP in-process che simula il servizio remoto "widgets".
 * Configura quarkus.rest-client.widgets-api.url puntandolo a sé stesso.
 *
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
                String body;
                if (path.endsWith("/header")) {
                    String header = exchange.getRequestHeaders().getFirst("X-Client");
                    body = "{\"echo\":\"" + (header == null ? "" : header) + "\"}";
                } else {
                    body = "{\"id\":1,\"name\":\"Gadget\"}";
                }
                byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            });
            server.start();
            port = server.getAddress().getPort();
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile avviare il server di test", e);
        }
        return Map.of("quarkus.rest-client.widgets-api.url", "http://localhost:" + port);
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}