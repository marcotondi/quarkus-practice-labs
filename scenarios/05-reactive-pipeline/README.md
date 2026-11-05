# Scenario 05 — Reactive Messaging Pipeline with Acknowledgment

## Scenario

Una stanza di compensazione riceve transazioni finanziarie da un canale
di messaggistica. Ogni transazione valida va salvata su PostgreSQL e solo
dopo deve essere pubblicata sul canale di uscita. Una transazione non
valida non va salvata e non va pubblicata. Il servizio non deve
confermare la ricezione di un messaggio prima che la scrittura sia
realmente completata.

Il progetto compila e si avvia, ma la pipeline non elabora nulla: nessuna
transazione viene persistita, nessun messaggio viene pubblicato.

## Obiettivi EX378 coperti

- SmallRye Reactive Messaging: canali in-memory, `@Incoming`,
  `@Outgoing`, acknowledgment manuale.
- Mutiny: `Uni`, `flatMap`, composizione asincrona, transazioni
  reattive.
- Persistenza reattiva con Hibernate Reactive Panache su PostgreSQL.
- Gestione di validazione, duplicati e propagazione degli errori nel
  flusso reattivo.

## Prerequisiti

Il servizio usa PostgreSQL reale. Nei test il database viene avviato
automaticamente dai Dev Services Quarkus tramite Podman o Docker. Con
Podman rootless servono queste variabili prima di eseguire i test:

```bash
export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock
export TESTCONTAINERS_RYUK_DISABLED=true
```

L'immagine PostgreSQL viene scaricata al primo avvio.

## Requisiti tecnici dettagliati

### Canali

| Canale | Direzione | Uso |
|---|---|---|
| `orders-in` | ingresso | transazioni inviate dai client |
| `orders-processed` | uscita | transazioni validate e persistite |

I canali sono in-memory: non esiste un connettore da dichiarare nelle
property. Non aggiungere `mp.messaging.*` per i canali.

### Evento

```json
{
  "transactionId": "TX-1",
  "amount": 100.00,
  "currency": "EUR"
}
```

Regole:

- `transactionId` obbligatorio e univoco nel database;
- `amount` obbligatorio e strettamente positivo;
- `currency` obbligatoria, tre lettere maiuscole.

### API REST

| Metodo | Path | Esiti |
|---|---|---|
| `POST` | `/transactions` | `202` evento accettato sul canale di ingresso |
| `GET` | `/transactions/{transactionId}` | `200` con lo stato della transazione; `404` assente |
| `GET` | `/audit/{transactionId}` | `200` con `deliveries` e `persistedWhenReceived`; `404` se il messaggio non è mai uscito |

`GET /audit/{transactionId}` è fornito dall'applicazione e non va
modificato.

La `POST` non deve attendere l'elaborazione: risponde `202` appena
l'evento è inviato al canale.

`GET /transactions/{transactionId}` restituisce almeno:

```json
{
  "transactionId": "TX-1",
  "amount": 100.00,
  "currency": "EUR",
  "status": "PROCESSED"
}
```

### Elaborazione e acknowledgment

- Strategia di acknowledgment **manuale** sul consumo di `orders-in`.
- Il messaggio di ingresso viene confermato solo **dopo** il commit della
  transazione reattiva che salva la transazione.
- La transazione viene pubblicata su `orders-processed` solo dopo il
  commit: `GET /audit/{transactionId}` risponde `persistedWhenReceived:
  true` solo se, al momento della ricezione del messaggio di uscita, il
  record era già visibile sul database.
- Importo o valuta non validi: il messaggio viene scartato, senza
  persistenza e senza messaggio su `orders-processed`. Lo scarto non deve
  interrompere il canale `orders-in`: gli eventi successivi devono
  continuare a essere elaborati.
- `transactionId` duplicato: nessun secondo record e nessun nuovo
  messaggio su `orders-processed`.
- `deliveries` su `/audit/{transactionId}` conta i messaggi di output
  ricevuti per quell'identificativo e vale `1` anche dopo un duplicato.
- Errore di persistenza: il messaggio di ingresso non deve risultare
  confermato.

### Contesto Vert.x

Un handler di messaggistica gira su un contesto Vert.x duplicato. Per
usare Hibernate Reactive Panache da quel contesto il contesto deve essere
marcato come sicuro. `ProcessedOrdersAudit` mostra già come farlo per la
parte di verifica.

### Persistenza

- Entità `Transaction`, tabella `transactions`, id di business
  `transactionId` con vincolo di unicità.
- Stato ammesso: `PROCESSED`.
- Database PostgreSQL avviato dai Dev Services; nessuna configurazione
  datasource nel progetto.

## Istruzioni di verifica

```bash
export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock
export TESTCONTAINERS_RYUK_DISABLED=true

./mvnw test
```

Avvio manuale (con un PostgreSQL già raggiungibile e configurato):

```bash
./mvnw quarkus:dev
curl -i -X POST http://localhost:8080/transactions \
  -H 'Content-Type: application/json' \
  -d '{"transactionId":"TX-1","amount":100.00,"currency":"EUR"}'
curl -s http://localhost:8080/transactions/TX-1
curl -s http://localhost:8080/audit/TX-1
```

## Regole del laboratorio

- Modificare solo codice in `src/main`.
- Non modificare i test né `ProcessedOrdersAudit`.
- Non aggiungere dipendenze.
- Non sostituire PostgreSQL con un database in-memory.
- Non dichiarare connettori in-memory nelle property: i canali sono
  automatici.
- I test usano identificativi distinti e non dipendono dall'ordine di
  esecuzione.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (60%): tutti i casi superati.
- **Correttezza del dominio** (20%): validazione prima della persistenza,
  ack solo dopo il commit, nessuna pubblicazione su errore, gestione dei
  duplicati, `202` immediato sulla `POST`.
- **Best practice** (20%): uso corretto di `Uni` senza `await` bloccanti
  nel codice di produzione, transazioni reattive, nessun `System.out`,
  DTO separati dall'entità.
