package io.github.marcotondi.labs.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: strumenta il servizio con le metriche Micrometer.
 */
@ApplicationScoped
public class OrderMetricsService {

    @Inject
    MeterRegistry registry;

    // TODO: Implementare qui
    // Conta le chiamate con la metrica "orders.placed"
    // (annotazione Micrometer @Counted).
    public String placeOrder(String sku) {
        return "placed:" + sku;
    }

    // TODO: Implementare qui
    // Misura la durata con la metrica "orders.processing"
    // (annotazione Micrometer @Timed).
    public String processOrder(String sku) {
        return "processed:" + sku;
    }

    // TODO: Implementare qui
    // Incrementa un Counter custom chiamato "orders.checkout".
    public String checkout(String sku) {
        return "checked-out:" + sku;
    }

    // TODO: Implementare qui
    // Misura la durata con un Timer custom chiamato "orders.lookup".
    public String lookup(String sku) {
        return "found:" + sku;
    }
}