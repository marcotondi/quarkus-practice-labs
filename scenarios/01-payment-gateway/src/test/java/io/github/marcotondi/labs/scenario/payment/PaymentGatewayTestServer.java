package io.github.marcotondi.labs.scenario.payment;

import com.sun.net.httpserver.HttpServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gateway di pagamento simulato, usato dai test.
 *
 * NON modificare.
 */
public class PaymentGatewayTestServer implements QuarkusTestResourceLifecycleManager {

    private static PaymentGatewayTestServer instance;

    private final AtomicInteger calls = new AtomicInteger();
    private final AtomicInteger remainingFailures = new AtomicInteger();

    private volatile long delayMs;
    private HttpServer server;

    public static PaymentGatewayTestServer instance() {
        return instance;
    }

    public void reset() {
        calls.set(0);
        remainingFailures.set(0);
        delayMs = 0;
    }

    public void failTimes(int times) {
        remainingFailures.set(times);
    }

    public void alwaysFail() {
        remainingFailures.set(Integer.MAX_VALUE);
    }

    public void delay(long milliseconds) {
        delayMs = milliseconds;
    }

    public int calls() {
        return calls.get();
    }

    @Override
    public Map<String, String> start() {
        try {
            server = HttpServer.create(new InetSocketAddress(0), 0);
            server.setExecutor(Executors.newCachedThreadPool());
            server.createContext("/payments", exchange -> {
                calls.incrementAndGet();
                exchange.getRequestBody().readAllBytes();
                if (delayMs > 0) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                if (remainingFailures.getAndUpdate(n -> n > 0 ? n - 1 : 0) > 0) {
                    respond(exchange, 500, "{\"error\":\"GATEWAY_ERROR\"}");
                } else {
                    respond(exchange, 200, "{\"paymentId\":\"pay-1\",\"status\":\"APPROVED\"}");
                }
            });
            server.start();
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile avviare il gateway di test", e);
        }
        instance = this;
        return Map.of("quarkus.rest-client.payment-api.url", "http://localhost:" + server.getAddress().getPort());
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
        instance = null;
        if (server != null) {
            server.stop(0);
        }
    }
}
