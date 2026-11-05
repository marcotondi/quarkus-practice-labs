package io.github.marcotondi.labs.scenario.reactivepipeline;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class TransactionProcessor {

    @Incoming("orders-in")
    @Outgoing("orders-processed")
    public TransactionEvent process(TransactionEvent event) {
        return null;
    }
}
