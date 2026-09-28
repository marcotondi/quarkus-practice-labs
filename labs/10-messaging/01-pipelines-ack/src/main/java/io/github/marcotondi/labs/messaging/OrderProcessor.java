package io.github.marcotondi.labs.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: completa la pipeline reattiva.
 */
@ApplicationScoped
public class OrderProcessor {

    private final List<String> processed = new CopyOnWriteArrayList<>();

    // TODO: Implementare qui
    // Aggiungi @Outgoing("orders-out") (il metodo ha già
    // @Incoming("orders-in")) per inoltrare il risultato trasformato.
    @Incoming("orders-in")
    public String transform(String order) {
        return "processed:" + order;
    }

    // TODO: Implementare qui
    // Aggiungi @Incoming("orders-out") per ricevere il messaggio
    // trasformato; poi salvalo e confermalo con message.ack().
    public CompletionStage<Void> collect(Message<String> message) {
        processed.add(message.getPayload());
        return message.ack();
    }

    public List<String> processed() {
        return List.copyOf(processed);
    }
}