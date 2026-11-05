# Scenario 02 — Secured Patient Records & Audit Observability

## Scenario

Il reparto IT di un ospedale espone le schede pazienti come API REST. Il
servizio deve essere accessibile solo a personale autenticato, con
permessi diversi per medici e amministratori. La direzione sanitaria
chiede inoltre due cose: sapere quanti tentativi di accesso vengono
negati, per ruolo, e poter correlare ogni operazione all'utente che
l'ha eseguita.

Il progetto compila e si avvia, ma non applica né autorizzazione né
validazione, non publica metriche e non propaga l'utente nel contesto di
auditing.

## Obiettivi EX378 coperti

- Sicurezza JWT Bearer con `quarkus-smallrye-jwt` e RBAC con ruoli.
- Bean Validation sul boundary REST.
- Metriche custom con Micrometer ed esposizione Prometheus.
- Propagazione del contesto con OpenTelemetry `Baggage`.
- Persistenza Panache e DTO separati dall'entità.

## Requisiti tecnici dettagliati

### Autenticazione

- Token JWT firmati, verificati con la chiave pubblica in
  `publicKey.pem`.
- Issuer atteso: `https://labs.marcotondi.github.io`.
- Ruoli letti dai gruppi del token.
- Username dell'utente: claim `preferred_username`.
- Richiesta senza token su endpoint protetto: `401`.

### API REST

| Metodo | Path | Ruoli ammessi | Esiti |
|---|---|---|---|
| `POST` | `/patients` | `doctor`, `admin` | `201` creato; `400` payload non valido; `401`; `403` |
| `GET` | `/patients/{id}` | `doctor`, `admin` | `200`; `404` assente; `401`; `403` |
| `DELETE` | `/patients/{id}` | solo `admin` | `204`; `404` assente; `401`; `403` |

`PatientRequest`:

```json
{
  "firstName": "Giulia",
  "lastName": "Bianchi",
  "fiscalCode": "BNCGLI90E57F205X",
  "birthDate": "1990-05-17"
}
```

`PatientResponse`:

```json
{
  "id": 1,
  "firstName": "Giulia",
  "lastName": "Bianchi",
  "fiscalCode": "BNCGLI90E57F205X",
  "birthDate": "1990-05-17",
  "auditUser": "alice"
}
```

### Validazione

| Campo | Regola |
|---|---|
| `firstName` | obbligatorio, lunghezza tra 2 e 50 |
| `lastName` | obbligatorio, lunghezza tra 2 e 50 |
| `fiscalCode` | obbligatorio, pattern `^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$` |
| `birthDate` | obbligatoria e nel passato |

Un payload non valido restituisce `400` e non deve creare alcun record.

### Metriche

- Contatore Micrometer di nome logico `patients.access.denied`.
- Nome Prometheus atteso: `patients_access_denied_total`.
- Tag obbligatorio `role`, con il ruolo dell'utente che ha ricevuto il
  `403`.
- Una richiesta respinta con `403` incrementa il contatore per ogni ruolo
  dell'utente.
- Una richiesta anonima respinta con `401` **non** incrementa il
  contatore e non deve produrre una serie con ruolo `anonymous`.
- Le richieste autorizzate non incrementano il contatore.

Metrica leggibile su `GET /q/metrics`, per esempio:

```text
patients_access_denied_total{role="nurse"} 1.0
```

### Auditing

- Il valore esposto in `auditUser` è lo username del token.
- Lo username deve essere propagato nel contesto distribuito tramite
  OpenTelemetry `Baggage` con chiave `username`.
- Il campo `auditUser` deve riportare il valore letto dal `Baggage`
  della richiesta corrente.
- Richieste consecutive di utenti diversi non devono contaminare il
  contesto: due chiamate di fila devono riportare ciascuna il proprio
  utente.

### Persistenza

- Entità `Patient` con Panache.
- Table `patients`.
- Nessuna entità esposta: si risponde sempre con `PatientResponse`.

## Istruzioni di verifica

```bash
./mvnw test
```

Avvio manuale (token da ottenere da un issuer reale o da firmare con la
chiave privata dei test):

```bash
./mvnw quarkus:dev
curl -i http://localhost:8080/patients/1 \
  -H "Authorization: Bearer <token>"
curl -s http://localhost:8080/q/metrics | grep patients_access_denied
```

## Regole del laboratorio

- Modificare solo il codice in `src/main`.
- Non modificare i test, le chiavi o la configurazione dei test.
- Non aggiungere dipendenze.
- Non disabilitare la validazione né rendere pubblici gli endpoint.
- I test creano i dati direttamente sul database e non dipendono
  dall'ordine di esecuzione.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (60%): tutti i casi superati.
- **Correttezza del dominio** (20%): ruoli corretti, `401` distinto dal
  `403`, metriche per ruolo senza incrementi anomali, auditing coerente
  con il token.
- **Best practice** (20%): DTO separati dall'entità, nessun `System.out`,
  gestione esplicita del `404`, nessun import inutilizzato.
