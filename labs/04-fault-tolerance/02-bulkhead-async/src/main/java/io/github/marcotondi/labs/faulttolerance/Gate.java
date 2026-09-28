package io.github.marcotondi.labs.faulttolerance;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Cancello di sincronizzazione per i test: permette di far bloccare
 * una chiamata e di sapere quando è "entrata".
 *
 * NON modificare.
 */
@ApplicationScoped
public class Gate {

    private volatile CountDownLatch entered = new CountDownLatch(1);
    private volatile CountDownLatch release = new CountDownLatch(1);

    public void reset() {
        entered = new CountDownLatch(1);
        release = new CountDownLatch(1);
    }

    /** Blocca finché il test non chiama release(), poi ritorna. */
    public String enterAndBlock(String id) {
        entered.countDown();
        try {
            if (!release.await(5, TimeUnit.SECONDS)) {
                return "timeout:" + id;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
        return "processed:" + id;
    }

    public boolean awaitEntered(long timeout, TimeUnit unit) throws InterruptedException {
        return entered.await(timeout, unit);
    }

    public void release() {
        release.countDown();
    }
}