package io.github.marcotondi.labs.faulttolerance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: aggiungi @Asynchronous e @Bulkhead.
 */
@ApplicationScoped
public class LoadService {

    @Inject
    Gate gate;

    // TODO: Implementare qui
    // - @Asynchronous: esegue su un thread del pool
    // - @Bulkhead(value = 1, waitingTaskQueue = 1): una sola esecuzione
    //   concorrente + un posto in coda; le chiamate oltre la coda
    //   vengono rifiutate
    // - @Fallback(fallbackMethod = "rejected"): le chiamate rifiutate
    //   ritornano "rejected:<id>"
    public CompletionStage<String> process(String id) {
        return CompletableFuture.completedFuture(gate.enterAndBlock(id));
    }

    // TODO: Implementare qui
    // - @Asynchronous: il nome del thread deve essere diverso da quello
    //   del chiamante
    public CompletionStage<String> threadName() {
        return CompletableFuture.completedFuture(Thread.currentThread().getName());
    }

    CompletionStage<String> rejected(String id) {
        return CompletableFuture.completedFuture("rejected:" + id);
    }
}