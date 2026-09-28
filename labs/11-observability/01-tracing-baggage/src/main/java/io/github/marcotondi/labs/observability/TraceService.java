package io.github.marcotondi.labs.observability;

import io.opentelemetry.api.trace.Tracer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: usa il Tracer e propaga un valore tramite Baggage.
 */
@ApplicationScoped
public class TraceService {

    @Inject
    Tracer tracer;

    // TODO: Implementare qui
    // 1. apri uno span:  tracer.spanBuilder("handle").startSpan()
    // 2. rendilo current: try (Scope scope = span.makeCurrent()) { ... }
    // 3. metti requestId nel baggage "request-id" e rendilo current:
    //      Baggage baggage = Baggage.current().toBuilder()
    //              .put("request-id", requestId).build();
    //      try (Scope s = baggage.storeInContext(Context.current()).makeCurrent()) { ... }
    // 4. ritorna:
    //      new TraceResult(
    //          Baggage.current().getEntryValue("request-id"),
    //          Span.current().getSpanContext().getTraceId());
    // 5. chiudi lo span con span.end() nel finally.
    public TraceResult handle(String requestId) {
        return null;
    }
}