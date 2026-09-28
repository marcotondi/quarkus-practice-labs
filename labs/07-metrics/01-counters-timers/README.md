# Esercizio 07-01 — Counters & Timers (Micrometer)

## Scenario

Il servizio ordini non espone nessuna metrica: il team di osservabilità
non può monitorare né il numero di ordini né i tempi di elaborazione.
Strumenta il servizio con **Micrometer** e verifica le metriche
sull'endpoint `/q/metrics`.

## Comportamento atteso

| Operazione | Metrica attesa (nome Prometheus) |
|------------|----------------------------------|
| `placeOrder(sku)` | `orders_placed_total` (annotazione `@Counted("orders.placed")`) |
| `processOrder(sku)` | `orders_processing_seconds_count` (annotazione `@Timed("orders.processing")`) |
| `checkout(sku)` | `orders_checkout_total` (Counter custom `orders.checkout`) |
| `lookup(sku)` | `orders_lookup_seconds_count` (Timer custom `orders.lookup`) |

## Istruzioni passo-passo

1. **Esplora**: `OrderMetricsService` (il file da completare),
   `OrderResource` (espone gli endpoint) e `MetricsTest`.
2. **Completa `OrderMetricsService.java`** — ci sono 4 `TODO`:
   - `@Counted("orders.placed")` su `placeOrder`;
   - `@Timed("orders.processing")` su `processOrder`;
   - Counter custom: `registry.counter("orders.checkout").increment()`;
   - Timer custom: `Timer.Sample` + `sample.stop(registry.timer("orders.lookup"))`.
3. **Verifica** che i test dell'esaminatore passino.

Le annotazioni sono `io.micrometer.core.annotation.Counted` /
`io.micrometer.core.annotation.Timed`.

## Comandi

```bash
# Avvio in modalità dev
./mvnw quarkus:dev
curl http://localhost:8080/api/orders/place/SKU-1
curl http://localhost:8080/q/metrics

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `OrderMetricsService.java` (eventuale config in
  `application.properties`).
- Vietato aggiungere dipendenze o toccare i file marcati "NON modificare".

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 4 i casi superati.
- **Correttezza** (30%): nomi delle metriche corretti, uso di
  annotazioni vs API programmatica dove appropriato, `Timer.Sample` usato
  correttamente (in `finally`).

Quando hai finito, mandami la tua soluzione (`OrderMetricsService.java`):
la correggo come un esaminatore e ti do punteggio + feedback.