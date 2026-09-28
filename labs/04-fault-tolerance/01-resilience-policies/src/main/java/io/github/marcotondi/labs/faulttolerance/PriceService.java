package io.github.marcotondi.labs.faulttolerance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: aggiungi la tolleranza ai guasti con MicroProfile Fault
 * Tolerance (@Retry, @Timeout, @CircuitBreaker, @Fallback).
 *
 * I parametri puoi metterli nelle annotazioni oppure in
 * application.properties (chiavi FaultTolerance).
 */
@ApplicationScoped
public class PriceService {

    @Inject
    DownstreamClient client;

    // TODO: Implementare qui
    // Deve ritentare i fallimenti transitori e, se tutti i tentativi
    // falliscono, restituire "price-unavailable" (fallback).
    public String getPrice(String sku) {
        return client.fetch(sku);
    }

    // TODO: Implementare qui
    // Deve fallire dopo un timeout breve (il downstream può impiegare
    // 500ms) e restituire "quote-unavailable" (fallback).
    public String getQuote(String sku) {
        return client.fetch("quote-" + sku);
    }

    // TODO: Implementare qui
    // Deve usare un circuit breaker: dopo alcuni fallimenti consecutivi
    // il circuito si apre e le chiamate falliscono subito (senza
    // contattare il downstream), restituendo "stock-unavailable".
    public String getStock(String sku) {
        return client.fetch("stock-" + sku);
    }
}