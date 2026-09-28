# Esercizio 11-01 — Tracing e Baggage (OpenTelemetry)

## Scenario

Vuoi tracciare l'elaborazione di una richiesta e **propagare un valore
contestuale** (`request-id`) lungo il trace, usando l'API OpenTelemetry:
`Tracer` per creare lo span e `Baggage` per il contesto.

## Comportamento atteso

`GET /api/trace/ABC` deve restituire:

| Campo | Valore |
|-------|--------|
| `requestId` | `ABC` (letto dal Baggage, quindi propagato) |
| `traceId` | un id di trace valido (32 cifre esadecimali, non nullo) |

## Istruzioni passo-passo

1. **Esplora**: `TraceService` (da completare), `TraceResource` (espone
   l'endpoint) e `TracingTest`.
2. **Completa `TraceService.handle`**:
   - apri uno span con `tracer.spanBuilder("handle").startSpan()`;
   - rendilo current con `try (Scope scope = span.makeCurrent())`;
   - costruisci il baggage `request-id` e rendilo current:
     ```java
     Baggage baggage = Baggage.current().toBuilder()
             .put("request-id", requestId).build();
     try (Scope s = baggage.storeInContext(Context.current()).makeCurrent()) {
         // ...
     }
     ```
   - ritorna `new TraceResult(Baggage.current().getEntryValue("request-id"),
     Span.current().getSpanContext().getTraceId())`;
   - chiudi lo span con `span.end()` nel `finally`.
3. **Verifica** che il test dell'esaminatore passi.

Le classi stanno in `io.opentelemetry.api.trace.*`,
`io.opentelemetry.api.baggage.*` e `io.opentelemetry.context.*`.

## Comandi

```bash
./mvnw quarkus:dev
curl http://localhost:8080/api/trace/ABC

# Test dell'esaminatore (deve passare)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `TraceService.java`.
- Vietato aggiungere dipendenze o toccare `TraceResource`/`TraceResult`.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): il caso superato.
- **Correttezza** (30%): span aperto e chiuso correttamente, `makeCurrent`
  con try-with-resources, baggage propagato nel context, lettura di
  `request-id` e `traceId` dallo span corrente.

Quando hai finito, mandami la tua soluzione (`TraceService.java`): la
correggo come un esaminatore.