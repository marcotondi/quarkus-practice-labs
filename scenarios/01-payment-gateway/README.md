# Scenario 01 — Order Payment Gateway & Resiliency

## Scenario

Il team `payments` sta riscrivendo il servizio ordini. Il servizio deve
registrare l'ordine, autorizzare il pagamento su un gateway esterno e
sopravvivere alle sue instabilità: latenze, errori transitori e blackout
prolungati.

Lo scheletro del progetto compila e si avvia, ma il flusso applicativo
non è implementato e nessuna policy di resilienza è attiva. I test
forniscono il contratto completo: falliscono finché il comportamento non
è quello descritto qui.

## Obiettivi EX378 coperti

- REST API con DTO e codici di stato coerenti.
- Persistenza Hibernate ORM con `PanacheRepository`.
- REST Client Reactive dichiarativo (`@RegisterRestClient`).
- MicroProfile Fault Tolerance: `@Timeout`, `@Retry`,
  `@CircuitBreaker`, `@Fallback`.
- SmallRye Health: readiness condizionata dallo stato del circuito.

## Requisiti tecnici dettagliati

### API REST

| Metodo | Path | Corpo | Esiti |
|---|---|---|---|
| `POST` | `/orders` | `OrderRequest` | `201` ordine creato e pagato; `400` richiesta non valida; `503` gateway non disponibile |
| `GET` | `/orders/{id}` | — | `200` ordine; `404` ordine inesistente |
| `GET` | `/q/health/ready` | — | `200` se il circuito non è aperto; `503` se è aperto |

`OrderRequest`:

```json
{ "amount": 125.00, "currency": "EUR" }
```

Vincoli:

- `amount` obbligatorio e strettamente positivo;
- `currency` obbligatoria, esattamente tre lettere maiuscole;
- un ordine non valido non deve mai raggiungere il gateway.

`OrderResponse`:

```json
{ "id": 1, "amount": 125.00, "currency": "EUR", "status": "PAID" }
```

Stati ammessi: `PENDING`, `PAID`, `FAILED_PAYMENT`.

Risposta `503`:

```json
{ "error": "PAYMENT_UNAVAILABLE", "orderId": 1 }
```

`orderId` è obbligatorio nella risposta `503`: l'ordine deve esistere nel
database con stato `FAILED_PAYMENT` anche quando la risposta è di errore.

### Persistenza

- Entità `Order` gestita tramite `OrderRepository implements
  PanacheRepository<Order>`.
- L'ordine nasce `PENDING`, passa a `PAID` solo dopo un pagamento
  approvato e a `FAILED_PAYMENT` quando il pagamento fallisce
  definitivamente.
- Il passaggio a `FAILED_PAYMENT` deve essere visibile via
  `GET /orders/{id}`: attenzione a cosa accade alla transazione quando
  l'errore viene propagato al client HTTP.
- Nostro servizio, nessuna entità esposta: si risponde sempre con
  `OrderResponse`.

### Gateway di pagamento

- Client dichiarativo `PaymentClient`, `configKey = "payment-api"`.
- URL del gateway nella property `quarkus.rest-client.payment-api.url`.
- Il gateway risponde `500` quando fallisce e `200` con
  `{"paymentId":"pay-1","status":"APPROVED"}` quando approva.
- La chiamata al gateway avviene fuori dal metodo `PaymentGateway.charge`
  solo per trasporto: la resilienza è responsabilità del progetto.

### Resilienza

Policy richieste sulla chiamata al gateway:

| Policy | Valori |
|---|---|
| Timeout | `800` millisecondi per singolo tentativo |
| Retry | `maxRetries = 3`, `delay = 200` millisecondi |
| Circuit breaker | `requestVolumeThreshold = 4`, `failureRatio = 0.5`, `delay = 5000` millisecondi, `successThreshold = 1` |
| Fallback | traduce il fallimento definitivo nell'errore `PAYMENT_UNAVAILABLE` |

Con `maxRetries = 3` il gateway riceve al massimo **quattro** chiamate per
singola richiesta HTTP: un tentativo iniziale più tre retry. Dopo la
quarta richiesta consecutiva fallita il circuito si apre e le richieste
successive non devono più raggiungere il gateway.

Il readiness check si chiama `payment-gateway` e deve restituire `DOWN`
quando il circuito è `OPEN`. Il circuito usato dalla policy è
identificato dal nome MicroProfile Fault Tolerance del metodo protetto:

```text
io.github.marcotondi.labs.scenario.payment.PaymentGateway/charge
```

Il progetto include già `PaymentUnavailableException` e `ErrorResponse`.
Come tradurre l'eccezione in risposta HTTP è una scelta implementativa.

## Istruzioni di verifica

```bash
./mvnw test
```

Avvio manuale, con un gateway reale raggiungibile sull'URL configurato:

```bash
./mvnw quarkus:dev
curl -i -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"amount":125.00,"currency":"EUR"}'
```

## Regole del laboratorio

- Modificare solo il codice in `src/main`.
- Non modificare i test né `PaymentGatewayTestServer`.
- Non aggiungere dipendenze, framework o librerie.
- Non introdurre configurazione del gateway hardcoded: l'URL arriva solo
  dalla property.
- I test sono indipendenti dall'ordine e resettano gateway simulato e
  circuito prima di ogni caso.
- La proprietà `quarkus.rest-client.payment-api.url` viene sovrascritta
  dal server di test.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (60%): tutti i casi superati.
- **Correttezza del dominio** (20%): stati e transizioni corretti,
  `orderId` presente nel `503`, nessuna chiamata al gateway per input
  invalidi, retry e circuit breaker con i valori richiesti.
- **Best practice** (20%): DTO separati dalle entità, nessun
  `System.out`, eccezioni di dominio, nessun import o codice inutilizzato,
  transazioni gestite correttamente.
