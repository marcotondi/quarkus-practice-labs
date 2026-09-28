# Laboratorio 04 — Fault Tolerance (MicroProfile)

## Scenario

Il servizio `pricing` chiama un servizio esterno instabile
(`DownstreamClient`): a volte fallisce, a volte è lento. Oggi ogni
problema del downstream si propaga all'utente come errore `500`. Devi
rendere il servizio **resiliente** con MicroProfile Fault Tolerance.

Il progetto compila, ma i test falliscono finché il task non è completo.

## Comportamento atteso

| Metodo | Endpoint | Requisito |
|--------|----------|-----------|
| `getPrice(sku)` | `/api/price/{sku}` | **@Retry**: tollera fino a 2 fallimenti transitori e riesce; se i tentativi si esauriscono → fallback `price-unavailable` |
| `getQuote(sku)` | `/api/quote/{sku}` | **@Timeout**: il downstream può impiegare 500ms; con timeout a 200ms → fallback `quote-unavailable` |
| `getStock(sku)` | `/api/stock/{sku}` | **@CircuitBreaker**: dopo 2 fallimenti consecutivi il circuito si apre e le chiamate falliscono subito senza contattare il downstream → fallback `stock-unavailable` |

## Istruzioni passo-passo

1. **Esplora**: leggi `DownstreamClient` (downstream instabile, con
   controlli per i test) e `PriceService` (il file da completare). I file
   marcati "NON modificare" sono completi.
2. **Completa `PriceService.java`** — ci sono 3 `TODO`. Aggiungi le
   annotazioni di Fault Tolerance (`@Retry`, `@Timeout`,
   `@CircuitBreaker`, `@Fallback`) e i relativi metodi di fallback.
   I parametri puoi metterli **nelle annotazioni** oppure in
   `application.properties` con le chiavi `FaultTolerance`, per esempio:
   ```properties
   io.github.marcotondi.labs.ft.PriceService/getQuote/Timeout/value=200
   ```
3. **Verifica** che i test dell'esaminatore passino.

Un metodo di fallback ha la **stessa firma** del metodo protetto (stessi
parametri e tipo di ritorno) e sta nella stessa classe.

## Comandi

```bash
# Avvio in modalità dev
./mvnw quarkus:dev        # http://localhost:8080/api/price/ABC

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `PriceService.java` (e, se vuoi, i parametri FT in
  `application.properties`).
- Vietato aggiungere dipendenze o toccare i file marcati "NON modificare".
- `DownstreamClient` espone `reset()`, `failTimes(n)`, `alwaysFail()`,
  `delay(ms)` e `calls()` usati dai test.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 4 i casi superati.
- **Correttezza FT** (30%): annotazioni appropriate per ogni scenario,
  fallback con firma corretta, parametri del circuit breaker coerenti
  (soglia 2, failure ratio 100%), timeout breve configurato.

Quando hai finito, mandami la tua soluzione (`PriceService.java`, ed
eventuali parametri in `application.properties`): la correggo come un
esaminatore e ti do punteggio + feedback.