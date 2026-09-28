package io.github.marcotondi.labs.faulttolerance;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simula un servizio esterno instabile (flaky), con comportamente
 * controllabile per i test.
 *
 * NON modificare.
 */
@ApplicationScoped
public class DownstreamClient {

    private final AtomicInteger calls = new AtomicInteger();
    private volatile int failuresRemaining = 0;
    private volatile boolean alwaysFail = false;
    private volatile long delayMillis = 0;

    /**
     * Chiamata al servizio esterno. Fallisce con DownstreamException
     * finché non sono esauriti i fallimenti programmati (o sempre, se
     * alwaysFail è attivo). Ritorna "price:<sku>" in caso di successo.
     */
    public String fetch(String sku) {
        calls.incrementAndGet();

        if (delayMillis > 0) {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DownstreamException("chiamata interrotta (timeout)");
            }
        }

        if (alwaysFail || failuresRemaining > 0) {
            if (failuresRemaining > 0) {
                failuresRemaining--;
            }
            throw new DownstreamException("downstream non disponibile per " + sku);
        }

        return "price:" + sku;
    }

    // --- controlli per i test e per la modalità dev ---

    public void reset() {
        calls.set(0);
        failuresRemaining = 0;
        alwaysFail = false;
        delayMillis = 0;
    }

    public DownstreamClient failTimes(int n) {
        failuresRemaining = n;
        return this;
    }

    public DownstreamClient alwaysFail() {
        alwaysFail = true;
        return this;
    }

    public DownstreamClient delay(long millis) {
        delayMillis = millis;
        return this;
    }

    public int calls() {
        return calls.get();
    }
}