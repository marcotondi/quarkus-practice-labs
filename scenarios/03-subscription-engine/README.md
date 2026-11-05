# Scenario 03 — Subscription Engine with ConfigMapping & Exception Mapping

## Scenario

Una piattaforma SaaS vende abbonamenti mensili con piani diversi. I
prezzi non sono nel codice: cambiano con il listino e devono essere
leggibili da configurazione tipizzata. Il servizio deve inoltre rifiutare
in modo esplicito il rinnovo di un abbonamento scaduto, con un errore di
dominio stabile per i client.

Il progetto compila e si avvia, ma la configurazione dei piani non è
mappata, il servizio non implementa le regole e un rinnovo impossibile
finirebbe in un errore generico.

## Obiettivi EX378 coperti

- MicroProfile Config con `@ConfigMapping` tipizzato e importi
  `BigDecimal`.
- Persistenza Panache con Active Record (`PanacheEntity`).
- Eccezione di dominio e `ExceptionMapper` JAX-RS.
- DTO separati dall'entità e codici HTTP coerenti.

## Requisiti tecnici dettagliati

### Configurazione dei piani

Le tariffe sono in `application.properties`:

```properties
subscription.plans.basic.monthly-price=19.90
subscription.plans.premium.monthly-price=49.90
```

Il listino deve essere letto tramite una mappatura tipizzata con prefisso
`subscription`, in modo che aggiungere un piano significhi aggiungere
solo property. Gli importi sono `BigDecimal`, non stringhe né `double`.

### API REST

| Metodo | Path | Esiti |
|---|---|---|
| `POST` | `/subscriptions` | `201` creato; `400` piano sconosciuto |
| `GET` | `/subscriptions/{id}` | `200`; `404` assente |
| `POST` | `/subscriptions/{id}/renew` | `200` rinnovato; `404` assente; `409` abbonamento scaduto |

`SubscriptionRequest`:

```json
{ "plan": "basic" }
```

`SubscriptionResponse`:

```json
{
  "id": 1,
  "plan": "basic",
  "status": "ACTIVE",
  "monthlyPrice": 19.90,
  "expiresAt": "2025-01-31"
}
```

Stati: `ACTIVE`, `EXPIRED`.

Risposta `409`:

```json
{ "error": "ABBONAMENTO_SCADUTO", "code": 409 }
```

### Regole di dominio

- Alla creazione: stato `ACTIVE`, prezzo letto dalla configurazione del
  piano, scadenza a **un mese** dalla data corrente.
- Piano non presente nella configurazione: `400`, nessuna persistenza.
- Rinnovo di abbonamento `ACTIVE`: la scadenza avanza di un mese e il
  prezzo viene ricalcolato dalla configurazione corrente del piano.
- Rinnovo di abbonamento `EXPIRED`: `409` con il body esatto sopra,
  nessuna modifica allo stato o alla scadenza.
- Rinnovo o lettura di un id assente: `404`.
- Nessuna entità esposta: si risponde sempre con `SubscriptionResponse`.

### Gestione degli errori

- L'eccezione `SubscriptionExpiredException` è già fornita.
- La traduzione in HTTP è a carico del progetto e deve produrre il body
  JSON documentato, non una pagina di errore generica.
- `ErrorResponse` è già fornito.

## Istruzioni di verifica

```bash
./mvnw test
```

Avvio manuale:

```bash
./mvnw quarkus:dev
curl -i -X POST http://localhost:8080/subscriptions \
  -H 'Content-Type: application/json' -d '{"plan":"premium"}'
curl -i -X POST http://localhost:8080/subscriptions/1/renew
```

## Regole del laboratorio

- Modificare solo il codice in `src/main`.
- Non modificare i test né `application.properties` per quanto riguarda
  le tariffe.
- Non aggiungere dipendenze.
- Non introdurre prezzi hardcoded nel codice Java.
- I test inseriscono i dati direttamente sul database e non dipendono
  dall'ordine di esecuzione.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (60%): tutti i casi superati.
- **Correttezza del dominio** (20%): prezzi dalla configurazione,
  scadenza a un mese, rinnovo che ricalcola il prezzo, `409` senza
  modifiche allo stato, `400` sul piano sconosciuto.
- **Best practice** (20%): DTO separati dall'entità, nessun `System.out`,
  mappatura tipizzata della configurazione, nessun import inutilizzato.
