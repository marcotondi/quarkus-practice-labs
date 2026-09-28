package io.github.marcotondi.labs.faulttolerance;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
class LoadServiceTest {

    @Inject
    LoadService service;

    @Inject
    Gate gate;

    @BeforeEach
    void resetGate() {
        gate.reset();
    }

    @Test
    void asynchronousRunsOnAnotherThread() throws Exception {
        String caller = Thread.currentThread().getName();
        String worker = service.threadName().toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertNotEquals(caller, worker, "@Asynchronous deve usare un altro thread");
    }

    @Test
    void bulkheadRejectsWhenFull() throws Exception {
        CompletionStage<String> first = service.process("A");
        assertTrue(gate.awaitEntered(5, TimeUnit.SECONDS), "la prima chiamata non è entrata");

        // bulkhead: 1 in esecuzione + 1 in coda -> la terza viene rifiutata
        CompletionStage<String> second = service.process("B");
        CompletionStage<String> third = service.process("C");
        assertEquals("rejected:C", third.toCompletableFuture().get(5, TimeUnit.SECONDS));

        gate.release();
        assertEquals("processed:A", first.toCompletableFuture().get(5, TimeUnit.SECONDS));
        assertEquals("processed:B", second.toCompletableFuture().get(5, TimeUnit.SECONDS));
    }
}