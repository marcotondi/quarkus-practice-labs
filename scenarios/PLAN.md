# Piano di implementazione — Scenari avanzati EX378

## 0. Stato del documento

- **Stato:** tutti e cinque gli scenari completati (traccia + soluzione locale verificate).
- **Cartella confermata:** `scenarios/`, allo stesso livello di `labs/`.
- **Baseline tecnica:** Quarkus **3.8.1**, Java **17**, Maven con wrapper `./mvnw`.
- **Obiettivo:** aggiungere cinque progetti hands-on multi-dominio in stile esame EX378.
- **Vincolo editoriale:** nessuna guida passo-passo, nessuno snippet risolutivo e nessun commento di aiuto nel codice Java degli scenari.
- **Vincolo Git:** `main` contiene solo le tracce; le soluzioni restano su branch locali e non vengono pushate.

Questo documento è la checklist operativa per la realizzazione. Le caselle vengono spuntate solo dopo avere eseguito davvero la verifica indicata.

---

## 1. Obiettivo e perimetro

### 1.1 Risultato atteso

Ogni scenario deve essere un progetto Maven Quarkus autonomo che:

1. compila sullo scheletro iniziale;
2. si avvia senza errori, fatte salve le dipendenze infrastrutturali documentate;
3. contiene test JUnit 5 che falliscono per comportamenti applicativi mancanti, non per errori di compilazione;
4. ha un `README.md` che descrive completamente il contratto da implementare;
5. ammette una soluzione intera verificabile con `./mvnw test`;
6. non richiede al candidato dipendenze, framework o librerie aggiuntive.

### 1.2 Differenza rispetto a `labs/`

| Cartella | Tipo di esercizio | Guida |
|---|---|---|
| `labs/` | Esercizi prevalentemente mono-dominio | Può essere progressiva e passo-passo |
| `scenarios/` | Scenari avanzati che combinano almeno tre domini EX378 | Solo requisiti, contratto, vincoli e verifica |

### 1.3 Fuori perimetro

- Pubblicazione delle soluzioni su `main` o sul remoto.
- Simulazioni d'esame complete con tempo limite o ambiente proctored.
- Pipeline CI/CD non richieste.
- Automazione del deploy su un cluster OpenShift reale.
- Introduzione di Lombok, framework aggiuntivi o dipendenze non indispensabili.
- Dynamic Logging nello scenario 03: non è presente un requisito osservabile sufficientemente definito e non aggiunge valore rispetto ai tre domini già coperti.

---

## 2. Struttura e convenzioni

### 2.1 Struttura da creare

```text
scenarios/
├── PLAN.md
├── 01-payment-gateway/
├── 02-patient-records/
├── 03-subscription-engine/
├── 04-inventory-openshift/
└── 05-reactive-pipeline/
```

Struttura standard di ogni progetto:

```text
scenarios/NN-<feature>/
├── README.md
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/wrapper/
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/
        └── resources/
```

Il Maven wrapper deve essere quello realmente utilizzato per la verifica. Se nei progetti esistenti è condiviso tramite una convenzione differente, adottare la stessa convenzione senza duplicazioni inutili, mantenendo valido il comando `./mvnw test` dalla cartella del progetto.

### 2.2 Naming

| Scenario | Cartella | `artifactId` | Package | Branch soluzione locale |
|---|---|---|---|---|
| 01 | `01-payment-gateway` | `scenario-01-payment-gateway` | `io.github.marcotondi.labs.scenario.payment` | `sol/scenario-01-payment-gateway` |
| 02 | `02-patient-records` | `scenario-02-patient-records` | `io.github.marcotondi.labs.scenario.patient` | `sol/scenario-02-patient-records` |
| 03 | `03-subscription-engine` | `scenario-03-subscription-engine` | `io.github.marcotondi.labs.scenario.subscription` | `sol/scenario-03-subscription-engine` |
| 04 | `04-inventory-openshift` | `scenario-04-inventory-openshift` | `io.github.marcotondi.labs.scenario.inventory` | `sol/scenario-04-inventory-openshift` |
| 05 | `05-reactive-pipeline` | `scenario-05-reactive-pipeline` | `io.github.marcotondi.labs.scenario.reactivepipeline` | `sol/scenario-05-reactive-pipeline` |

Regole comuni:

- `groupId`: `io.github.marcotondi.labs`.
- Directory solo minuscole, numeri e trattini.
- Dipendenza dal BOM `io.quarkus.platform:quarkus-bom:3.8.1`.
- `maven.compiler.release=17`.
- REST su Quarkus 3.8 tramite `quarkus-resteasy-reactive` e `quarkus-resteasy-reactive-jackson`.
- Test tramite `quarkus-junit5` e REST-Assured quando sono coinvolti endpoint HTTP.

### 2.3 Task iniziali

- [x] Creare la directory `scenarios/` senza spostare o rinominare i laboratori esistenti.
- [x] Preparare lo scaffold di `01-payment-gateway` seguendo i `pom.xml` dei progetti esistenti.
- [x] Verificare che il wrapper e il BOM producano una build Quarkus 3.8.1 effettiva.
- [x] Registrare in questo file eventuali deviazioni dal naming prima di replicare lo scaffold negli altri scenari.

---

## 3. Standard editoriale dei README

Ogni `scenarios/NN-<feature>/README.md` deve contenere le sezioni seguenti.

### 3.1 Titolo e descrizione dello scenario

- Contesto applicativo realistico.
- Problema da risolvere.
- Componenti esterni o infrastrutturali coinvolti.
- Prerequisiti specifici, per esempio Podman per PostgreSQL.

### 3.2 Obiettivi EX378 coperti

Elencare solo gli obiettivi effettivamente verificati dai test o dalla verifica di build. Non dichiarare coperto un dominio presente soltanto come dipendenza nel `pom.xml`.

### 3.3 Requisiti tecnici dettagliati

Il README deve fissare senza ambiguità:

- rotte HTTP, metodi, path parameter e corpi JSON;
- codici di risposta per successo, input non valido, autenticazione mancante, autorizzazione negata, risorsa assente e indisponibilità del downstream;
- nomi e struttura dei DTO esposti;
- stati di persistenza e transizioni consentite;
- nomi esatti delle property di configurazione;
- valori numerici di timeout, retry e circuit breaker;
- nomi dei canali messaging e strategia di acknowledgment;
- nomi delle metriche e relativi tag;
- nomi degli health check, gruppi ed endpoint;
- comportamento atteso in caso di eccezioni.

### 3.4 Istruzioni di verifica

Comando obbligatorio:

```bash
./mvnw test
```

Per lo scenario 04 aggiungere i comandi separati per generazione dei manifest e build dell'immagine. Per lo scenario 05 documentare il prerequisito Podman e l'eventuale avvio manuale di PostgreSQL se il fallback diventa necessario.

### 3.5 Regole e rubrica

- File che il candidato può modificare.
- Divieto di aggiungere dipendenze.
- Divieto di modificare i test e i test double, elencati nel README e non tramite commenti guida nel codice.
- Indipendenza dei test dall'ordine di esecuzione.
- Rubrica consigliata: test 60%, correttezza del dominio 20%, best practice 20%.

### 3.6 Divieti editoriali

Nei README degli scenari non inserire:

- istruzioni numerate che conducono direttamente alla soluzione;
- snippet con annotazioni o implementazioni finali;
- nomi di import da copiare;
- spiegazioni che trasformano il requisito in una ricetta.

Nel codice Java degli scheletri non inserire:

- `TODO`;
- commenti che indicano annotazioni, classi o strategie da usare;
- commenti con frammenti della soluzione;
- implementazioni fittizie commentate.

---

## 4. Standard dello scheletro e dei test

### 4.1 Scheletro iniziale

- Il codice deve compilare.
- Le classi previste dal dominio devono essere presenti solo nella misura necessaria a definire il punto di partenza.
- DTO, enum e modelli possono essere completi se rappresentano il contratto e non la logica da dedurre.
- Resource, service, client e processor possono contenere implementazioni neutre o incomplete, purché i test falliscano sul comportamento e non sulla compilazione.
- Non restituire dati che facciano passare accidentalmente un test.
- Non esporre direttamente entità di persistenza negli endpoint che prevedono DTO.
- Applicare Bean Validation al boundary REST quando richiesto; lo scheletro non deve contenere suggerimenti testuali al riguardo.

### 4.2 Test

- Usare JUnit 5 e `@QuarkusTest`.
- Usare REST-Assured per i contratti HTTP.
- Simulare i downstream HTTP con `QuarkusTestResourceLifecycleManager` e un server in-process, secondo il pattern già presente in `09-rest-client/01-declarative-client`.
- Firmare i JWT nei test con chiavi di test, secondo il pattern già presente in `06-security/01-jwt-rbac`.
- Utilizzare H2 per i test transazionali non reattivi, salvo requisiti differenti.
- Utilizzare PostgreSQL reale per lo scenario reattivo.
- Resettare stato, contatori e comportamento dei test double tra un test e l'altro.
- Evitare sleep lunghi e assertion temporali fragili; usare polling con timeout esplicito dove il comportamento è asincrono.
- Non dipendere dall'ordine dei test.
- Dare ai test nomi che esprimano il comportamento richiesto.

### 4.3 Significato del rosso iniziale

Prima di considerare pronta una traccia:

- [x] Eseguire `./mvnw test` sullo scheletro.
- [x] Verificare che il progetto compili.
- [x] Verificare che i test previsti falliscano.
- [x] Controllare che ogni fallimento corrisponda a un requisito del README.
- [x] Escludere fallimenti causati da infrastruttura mancante, porta occupata, configurazione errata o test non deterministico.
- [x] Annotare mentalmente o nel commit della traccia il numero di test eseguiti e falliti; non inserire output effimero nel README del candidato.

---

## 5. Protocollo obbligatorio rosso/verde

Per ogni scenario eseguire entrambe le verifiche seguenti.

### 5.1 Traccia su `main`

1. Partire da `main` aggiornato.
2. Creare lo scheletro e i test.
3. Eseguire `./mvnw test`.
4. Confermare il rosso atteso.
5. Eseguire, se applicabile, l'avvio o la generazione dei manifest.
6. Committare solo traccia, test e documentazione.

### 5.2 Soluzione locale

1. Creare il branch `sol/scenario-NN-<feature>` dalla traccia.
2. Implementare la soluzione senza modificare i test per renderli più permissivi.
3. Eseguire `./mvnw test` fino al verde reale.
4. Se un test è errato o eccessivamente rigido, correggerlo sulla traccia e ripetere sia il rosso sia il verde.
5. Controllare best practice: DTO separati dalle entità, errori gestiti, niente `System.out`, niente import o codice inutilizzato.
6. Non pushare il branch soluzione.
7. Tornare su `main` e verificare che l'albero contenga ancora solo la traccia.

### 5.3 Criterio di accettazione

Uno scenario è completo solo quando:

- [x] rosso iniziale verificato;
- [x] verde della soluzione verificato;
- [x] test ripetuti senza fallimenti intermittenti;
- [x] README coerente con il comportamento dei test;
- [x] soluzione assente da `main`;
- [x] documentazione aggiornata.

---

## 6. Scenario 01 — Order Payment Gateway & Resiliency

### 6.1 Domini e obiettivo

Domini:

- REST API e DTO.
- Persistenza con `PanacheRepository<Order>`.
- REST Client Reactive con `@RegisterRestClient`.
- MicroProfile Fault Tolerance: `@Timeout`, `@Retry`, `@CircuitBreaker`, `@Fallback`.
- SmallRye Health con readiness dipendente dallo stato del circuit breaker.

Flusso richiesto:

1. `POST /orders` valida e persiste un ordine in `PENDING`.
2. Il servizio chiama il gateway di pagamento esterno.
3. In caso di successo aggiorna lo stato a `PAID` e restituisce `201`.
4. Dopo il fallimento definitivo aggiorna lo stato a `FAILED_PAYMENT` e restituisce `503`.
5. Un readiness check riporta `DOWN` quando il circuit breaker è `OPEN`.

### 6.2 Contratto da fissare nel README

Endpoint:

| Operazione | Esito |
|---|---|
| `POST /orders` valido, pagamento riuscito | `201`, DTO ordine con stato `PAID` |
| `POST /orders` non valido | `400`, nessuna chiamata al gateway |
| Pagamento definitivamente non disponibile | `503`, errore `PAYMENT_UNAVAILABLE`, ordine `FAILED_PAYMENT` |
| `GET /orders/{id}` esistente | `200` con DTO ordine |
| `GET /orders/{id}` assente | `404` |
| `GET /q/health/ready` con circuito chiuso | `200` e stato `UP` |
| `GET /q/health/ready` con circuito aperto | `503` e stato `DOWN` |

Modello minimo:

- `id`
- `amount`, obbligatorio e positivo
- `currency`, obbligatorio, tre lettere maiuscole
- `status`: `PENDING`, `PAID`, `FAILED_PAYMENT`

Configurazione:

- `quarkus.rest-client.payment-api.url`: URL del gateway.
- Il test resource sovrascrive l'URL con il server in-process.

Policy di resilienza da rendere esplicite:

- timeout: **800 ms** per singola invocazione;
- retry: `maxRetries = 3`, `delay = 200 ms`, quindi una chiamata iniziale più tre retry;
- circuit breaker: `requestVolumeThreshold = 4`, `failureRatio = 0.5`, `delay = 5000 ms`, `successThreshold = 1`;
- nome del circuit breaker reso disponibile al readiness check tramite il meccanismo SmallRye FT supportato dalla versione in uso;
- il fallback aggiorna lo stato dell'ordine e traduce il fallimento nel contratto HTTP `503`.

### 6.3 Classi e risorse previste

- `Order`, `OrderStatus`, `OrderRepository`.
- DTO di richiesta/risposta ordine e pagamento.
- `PaymentClient`.
- `OrderService`.
- `OrderResource`.
- Eccezione e mapping applicativo per il pagamento non disponibile, se necessari alla separazione tra service e REST.
- `PaymentGatewayReadinessCheck`.
- `PaymentGatewayTestServer` nei test.
- Test REST e di resilienza.

### 6.4 Test minimi

- [x] Pagamento riuscito: `201`, stato `PAID`, record persistito.
- [x] Persistenza iniziale `PENDING` verificabile prima della risposta del downstream tramite il test double.
- [x] Input non valido: `400` e zero chiamate downstream.
- [x] Fallimento definitivo: `503` e stato `FAILED_PAYMENT`.
- [x] Retry: conteggio downstream coerente con una chiamata iniziale più tre retry.
- [x] Timeout: risposta oltre 800 ms trattata come fallimento secondo la policy.
- [x] Circuit breaker: apertura al rapporto di fallimenti configurato.
- [x] Readiness: `DOWN` con circuito `OPEN` e ritorno `UP` dopo il ripristino, senza rendere il test fragile.
- [x] Ordine inesistente: `404`.

### 6.5 Sottotask di implementazione

- [x] Creare progetto, POM, wrapper e configurazione H2.
- [x] Creare modello, DTO e classi dello scheletro senza commenti guida.
- [x] Implementare il downstream in-process con modalità successo, errore e ritardo, contatore chiamate e reset.
- [x] Scrivere i test del contratto REST e della persistenza.
- [x] Scrivere i test di retry, timeout, circuit breaker e readiness.
- [x] Scrivere il README con tutti i valori numerici e gli schemi JSON.
- [x] Verificare il rosso sullo scheletro.
- [x] Implementare e verificare la soluzione sul branch locale.
- [x] Ripetere i test almeno due volte per controllare la stabilità temporale.

### 6.6 Esiti e deviazioni registrate

- Rosso della traccia: 9 test su 9 falliti, tutti per `UnsupportedOperationException` nel service, nessun errore di compilazione o di avvio.
- Verde della soluzione: 9 test su 9 superati, suite ripetuta tre volte senza fallimenti intermittenti.
- `Order` è parola riservata in H2: l'entità usa `@Table(name = "orders")`. La correzione è nella traccia, non nella soluzione.
- Il gateway simulato deve valutare il fallimento **prima** del ritardo, altrimenti le richieste rimaste in volo dal caso di timeout consumano i fallimenti programmati del caso successivo.
- Il nome effettivo del circuito non è affidabile per il readiness check: la traccia richiede il nome esplicito `payment-gateway`, ottenibile con `@CircuitBreakerName`. Il README lo documenta come requisito, non come soluzione.
- Il timeout di 800 ms con retry produce quattro tentativi: il test accetta solo l'esito `503` e non il numero di chiamate, così non è legato ai tempi.

---

## 7. Scenario 02 — Secured Patient Records & Audit Observability

### 7.1 Domini e obiettivo

Domini:

- Autenticazione JWT Bearer e RBAC tramite `quarkus-smallrye-jwt`.
- Bean Validation sui DTO REST.
- Metriche custom con Micrometer `MeterRegistry`.
- OpenTelemetry `Baggage` per il contesto di auditing.
- Persistenza dei record necessaria a rendere gli endpoint realistici.

Decisione: non introdurre un provider OIDC esterno. I test firmano token JWT reali e verificano autenticazione e gruppi.

### 7.2 Contratto da fissare nel README

Endpoint:

| Operazione | Ruolo | Esiti |
|---|---|---|
| `POST /patients` | `doctor`, `admin` | `201`; input invalido `400`; anonimo `401`; altro ruolo `403` |
| `GET /patients/{id}` | `doctor`, `admin` | `200`; assente `404`; anonimo `401`; altro ruolo `403` |
| `DELETE /patients/{id}` | solo `admin` | `204`; assente `404`; `doctor` riceve `403`; anonimo `401` |

DTO di creazione:

- nome e cognome obbligatori e con limiti `@Size` esplicitati;
- codice fiscale obbligatorio, validato con il pattern esatto pubblicato nel README;
- data di nascita obbligatoria e nel passato;
- nessuna entità esposta direttamente.

Auditing:

- username letto da `preferred_username` del `JsonWebToken`;
- chiave Baggage: `username`;
- il valore deve essere inserito nel contesto OpenTelemetry durante l'operazione;
- il test deve osservare il valore tramite un contratto esplicito, per esempio il campo `auditUser` del DTO di risposta, senza affidarsi a stato globale residuo.

Metrica:

- nome logico Micrometer: `patients.access.denied`;
- nome Prometheus atteso: `patients_access_denied_total`;
- tag obbligatorio: `role`;
- un `403` per ruolo `doctor` incrementa la serie con `role="doctor"`;
- un `401` anonimo non incrementa una metrica di ruolo;
- nessun incremento per richieste autorizzate.

Configurazione JWT:

- issuer e public key location definiti nelle risorse;
- chiavi private usate solo dai test per firmare i token;
- nessun token statico condiviso tra i test.

### 7.3 Classi e risorse previste

- Entità e DTO paziente.
- Repository o meccanismo Panache scelto per la persistenza.
- `PatientService` e `PatientResource`.
- Componente per le metriche degli accessi negati.
- Componente/filter/mapper necessario a osservare i `403` con `SecurityIdentity`.
- Componente per l'auditing tramite `JsonWebToken` e Baggage.
- Chiavi di test e configurazione JWT.
- Test di sicurezza, validazione, metriche e auditing.

### 7.4 Test minimi

- [x] Accesso anonimo: `401` sugli endpoint protetti.
- [x] `doctor` può creare e leggere, ma non eliminare.
- [x] `admin` può creare, leggere ed eliminare.
- [x] Ruolo non autorizzato: `403`.
- [x] Payload non valido: `400`, incluso codice fiscale non conforme.
- [x] Paziente assente: `404`.
- [x] Metrica `patients_access_denied_total{role="doctor"}` incrementata solo dal `403` previsto.
- [x] Richiesta anonima senza incremento della serie per ruolo.
- [x] Baggage `username` coerente con `preferred_username` del token.
- [x] Due utenti consecutivi non contaminano il contesto di auditing.

### 7.5 Sottotask di implementazione

- [x] Creare progetto e dipendenze JWT, validation, Micrometer, OpenTelemetry e persistenza.
- [x] Preparare chiavi e helper di firma dei token nei test.
- [x] Creare modello, DTO, resource e service dello scheletro.
- [x] Scrivere test RBAC e validazione.
- [x] Scrivere test della metrica con reset o confronto del valore precedente.
- [x] Scrivere test del Baggage e dell'isolamento tra richieste.
- [x] Scrivere il README con ruoli, codici HTTP, pattern e nomi metriche.
- [x] Verificare il rosso iniziale.
- [x] Validare a runtime l'accesso a `SecurityIdentity` nel punto in cui vengono contati i `403`.
- [x] Implementare e verificare la soluzione locale.

### 7.6 Esiti

- Rosso: 9 test su 9 falliti, tutti per `UnsupportedOperationException` nel service.
- Verde: 9 test su 9, suite ripetuta tre volte.
- `ExceptionMapper<ForbiddenException>` riceve `SecurityIdentity`: la metrica per ruolo funziona nel mapper, senza filtri ulteriori.
- Le richieste anonime non producono una serie con ruolo `anonymous` perché il contatore incrementa solo i ruoli dell'identità.

---

## 8. Scenario 03 — Subscription Engine with ConfigMapping & Exception Mapping

### 8.1 Domini e obiettivo

Domini:

- MicroProfile Config con `@ConfigMapping` tipizzato.
- Panache Active Record tramite `PanacheEntity`.
- Exception personalizzata di business e JAX-RS `ExceptionMapper`.
- REST API e validazione degli input.

Il rinnovo di un abbonamento scaduto deve produrre un errore di dominio strutturato, non un generico `500`.

### 8.2 Contratto da fissare nel README

Property iniziali:

```properties
subscription.plans.basic.monthly-price=19.90
subscription.plans.premium.monthly-price=49.90
```

Il mapping deve essere tipizzato e usare `BigDecimal` per gli importi. Il candidato deve dedurre la struttura delle interfacce dal contratto, senza snippet risolutivi.

Endpoint:

| Operazione | Esito |
|---|---|
| `POST /subscriptions` con piano valido | `201`, prezzo letto dalla configurazione |
| `POST /subscriptions` con piano sconosciuto | `400` |
| `GET /subscriptions/{id}` | `200` oppure `404` |
| `POST /subscriptions/{id}/renew` su abbonamento attivo | `200`, scadenza estesa e prezzo ricalcolato dal piano |
| Rinnovo su abbonamento `EXPIRED` | `409` con JSON esatto |
| Rinnovo su id assente | `404` |

Risposta di conflitto:

```json
{
  "error": "ABBONAMENTO_SCADUTO",
  "code": 409
}
```

Regole:

- stati minimi `ACTIVE` ed `EXPIRED`;
- date e importi restituiti tramite DTO;
- nessuna esposizione diretta di `PanacheEntity`;
- eccezione dedicata `SubscriptionExpiredException`;
- nessun `System.out` o logging usato come sostituto della risposta di errore.

### 8.3 Classi e risorse previste

- `SubscriptionConfig` e mapping dei piani.
- `Subscription`, `SubscriptionStatus`.
- DTO di creazione, rinnovo e risposta.
- `SubscriptionService` e `SubscriptionResource`.
- `SubscriptionExpiredException`.
- Mapper JAX-RS dell'eccezione e DTO di errore.
- Configurazione H2 e test.

### 8.4 Test minimi

- [x] Creazione con piano `basic`: prezzo `19.90` dalla configurazione.
- [x] Creazione con piano `premium`: prezzo `49.90` dalla configurazione.
- [x] Piano sconosciuto: `400` e nessuna persistenza.
- [x] Rinnovo di abbonamento attivo: `200` e nuova scadenza coerente.
- [x] Rinnovo di abbonamento scaduto: `409`, body esatto e nessuna modifica allo stato.
- [x] Risorsa assente: `404`.
- [x] La risposta REST non espone campi interni dell'entità non previsti dal DTO.

### 8.5 Sottotask di implementazione

- [x] Creare progetto e dipendenze REST, Panache, H2 e validation.
- [x] Definire property e scheletro del mapping senza istruzioni risolutive.
- [x] Creare entità, DTO, service, resource ed eccezione.
- [x] Scrivere i test di mapping dei prezzi e delle regole di rinnovo.
- [x] Scrivere i test del mapper e del body `409`.
- [x] Scrivere il README con regole temporali deterministiche.
- [x] Verificare il rosso iniziale.
- [x] Implementare e verificare la soluzione locale.

### 8.6 Esiti

- Rosso: 8 test su 8 falliti.
- Verde: 8 test su 8, suite ripetuta tre volte.
- Un `@Consumes(APPLICATION_JSON)` a livello di classe manda in `415` la `POST /subscriptions/{id}/renew`, che non ha body: va dichiarato solo sul metodo con body.
- Il test sul conteggio dei record era troppo accoppiato ai dati di altri test: sostituito con una verifica per `id`.

---

## 9. Scenario 04 — Inventory Tracking with OpenShift & Health Groups

### 9.1 Domini e obiettivo

Domini:

- Custom Health Check `@Liveness` e `@Readiness`.
- SmallRye Health Groups con `@HealthGroup("deep-checks")`.
- Configurazione `quarkus-openshift`.
- Configurazione di Route, probe e variabili da ConfigMap.
- Build dell'immagine container con l'infrastruttura Podman disponibile.

### 9.2 Perimetro verificabile e limiti dell'ambiente

Verifiche automatiche o locali obbligatorie:

- `/q/health/live` e `/q/health/ready`.
- `/q/health/group/deep-checks`.
- Health check JVM/memoria e database.
- Generazione di `target/kubernetes/openshift.yml`.
- Presenza nel manifest di Route, probe e riferimenti alla ConfigMap.
- Build dell'immagine container tramite un builder Quarkus 3.8.1 effettivamente compatibile con Podman.

Non dichiarare come verificato:

- deploy su OpenShift;
- stato `Ready` di un pod;
- funzionamento della Route pubblica;
- compatibilità con Minishift.

Minishift è documentabile solo come esercizio manuale perché è associato a OKD 3.11 ed è obsoleto. OpenShift Local/CRC è l'alternativa raccomandata per un deploy reale.

### 9.3 Contratto da fissare nel README

Health:

| Endpoint | Verifica |
|---|---|
| `/q/health/live` | Memoria JVM sufficiente secondo una soglia configurabile |
| `/q/health/ready` | Connessione al database attiva |
| `/q/health/group/deep-checks` | Entrambi i controlli profondi raggruppati e nominalmente identificabili |

Requisiti OpenShift:

- Route esposta.
- Liveness probe su `/q/health/live`.
- Readiness probe su `/q/health/ready`.
- URL del database iniettato da una ConfigMap.
- Nome della ConfigMap e chiavi attese documentati.
- Nessuna password reale nel repository.

Le property `quarkus.openshift.*` devono essere controllate contro la documentazione/configurazione Quarkus **3.8.1** prima di scrivere il README. Non copiare nomi da versioni più recenti.

Configurazione runtime:

- H2 consentito nei test per rendere deterministici gli health check.
- Profilo PostgreSQL per il deployment documentato.
- Soglia memoria configurabile tramite una property del dominio `inventory`.

### 9.4 Classi e risorse previste

- Minimo modello/resource inventory necessario a rendere il servizio avviabile.
- Health check memoria.
- Health check database.
- Configurazione health group.
- `application.properties` con profilo OpenShift/PostgreSQL.
- Dipendenze SmallRye Health, OpenShift e container image.
- Test degli endpoint health.
- Appendice README per il deploy manuale.

### 9.5 Test e verifiche minime

- [x] Liveness disponibile e `UP` nella configurazione normale.
- [x] Readiness disponibile e `UP` con database di test attivo.
- [x] Gruppo `deep-checks` esposto e contenente i check previsti.
- [x] Stato `DOWN` verificato tramite una condizione di test controllabile, senza tentare di esaurire davvero la memoria JVM.
- [x] Build Maven completata.
- [x] Manifest OpenShift generato e ispezionato.
- [x] Route, probe e ConfigMap presenti nel manifest.
- [x] Build container eseguita con Podman oppure limite documentato con la causa reale.

### 9.6 Sottotask di implementazione

- [x] Verificare nella documentazione 3.8.1 i nomi delle property OpenShift e del builder container.
- [x] Creare progetto e configurazione health/database.
- [x] Creare gli health check e lo scheletro inventory.
- [x] Scrivere i test di liveness, readiness e gruppo.
- [x] Configurare OpenShift, Route, probe e ConfigMap.
- [x] Scrivere il README separando requisiti verificati e appendice di deploy.
- [x] Verificare il rosso iniziale.
- [x] Implementare e verificare la soluzione locale.
- [x] Eseguire `./mvnw package` e controllare il manifest generato.
- [x] Eseguire la build dell'immagine e registrare il comando effettivamente riuscito nel README.

### 9.7 Esiti

- Rosso: 7 test su 7 falliti (4 failure, 3 error di configurazione mancante).
- Verde: 7 test su 7.
- I path `checks[0]` non sono affidabili: su `/q/health/ready` convivono check di estensioni diverse. Usare `checks.find { it.name == '...' }`.
- Build immagine: `podman build -f src/main/docker/Dockerfile.jvm` è il comando verificato. Il builder Quarkus richiede `-Dquarkus.container-image.builder=docker` quando c'è anche `quarkus-openshift`, e su questa macchina il wrapper Docker usa buildx con driver `docker-container`, quindi l'immagine non finisce nello store Podman.
- Manifest generato e ispezionato: `kind: Route`, `kind: ConfigMap`, `livenessProbe`, `readinessProbe`, `envFrom.configMapRef`.
- Minishift non è installato e non è stato testato: il deploy resta manuale e non verificato.

---

## 10. Scenario 05 — Reactive Messaging Pipeline with Acknowledgment

### 10.1 Domini e obiettivo

Domini:

- SmallRye Reactive Messaging con `@Incoming` e `@Outgoing`.
- Mutiny `Uni` per l'elaborazione asincrona.
- Hibernate Reactive/Panache reactive e PostgreSQL.
- Message Acknowledgment manuale dopo la scrittura su database.
- Gestione di acknowledgment negativo per errori di validazione o persistenza.

### 10.2 Gate infrastrutturale preliminare

Podman è disponibile in modalità rootless e il socket utente risulta attivo. Prima di scrivere lo scenario:

- [x] Verificare l'accesso al socket `$XDG_RUNTIME_DIR/podman/podman.sock`.
- [x] Verificare il pull e l'avvio di un container PostgreSQL.
- [x] Provare un progetto Quarkus 3.8.1 minimo con Dev Services PostgreSQL.
- [x] Se necessario, impostare `DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock` e ripetere.
- [x] Verificare che Hibernate Reactive e il driver PostgreSQL reattivo 3.8.1 vengano risolti da Maven.

Se Dev Services non riconosce Podman:

1. non sostituire silenziosamente PostgreSQL con un ledger in-memory;
2. configurare `quarkus.datasource.devservices.enabled=false`;
3. documentare nel README l'avvio esplicito del container PostgreSQL con Podman;
4. configurare URL, credenziali e schema di test;
5. ripetere il gate fino a ottenere test realmente eseguiti contro PostgreSQL.

### 10.3 Contratto da fissare nel README

Canali:

- ingresso: `orders-in`;
- uscita: `orders-processed`.

Evento minimo:

- `transactionId` obbligatorio e univoco;
- `amount` obbligatorio;
- `currency` obbligatorio.

Regole:

- importo positivo: transazione valida, persistenza con stato `PROCESSED`, pubblicazione del messaggio trasformato e acknowledgment solo dopo il completamento della scrittura;
- importo nullo, zero o negativo: nessuna persistenza come transazione processata, nessun output di successo e acknowledgment negativo secondo il contratto pubblicato;
- errore di persistenza: nessun acknowledgment positivo e propagazione del fallimento secondo la strategia documentata;
- strategia esplicita `@Acknowledgment(Acknowledgment.Strategy.MANUAL)`;
- nessun acknowledgment anticipato prima del completamento del `Uni` di persistenza.

Per consentire la verifica, prevedere un endpoint di sola lettura o un repository testabile:

- `GET /transactions/{transactionId}` restituisce `200` con lo stato oppure `404`.

### 10.4 Classi e risorse previste

- Evento/DTO della transazione.
- Entità reattiva e repository/Panache reactive.
- Processor della pipeline.
- Resource di lettura dello stato.
- Configurazione datasource PostgreSQL.
- In-memory connector per i canali nei test.
- Test di integrazione con PostgreSQL.
- Test double o `Message` controllabile per osservare ack/nack.

### 10.5 Test minimi

- [x] Transazione valida: record `PROCESSED` presente su PostgreSQL.
- [x] Messaggio di output pubblicato solo dopo la persistenza.
- [x] Acknowledgment positivo completato dopo il commit.
- [x] Importo non valido: nessun output di successo e nessuna persistenza processata.
- [x] Fallimento di persistenza: nessun acknowledgment positivo.
- [x] Eventi multipli elaborati senza dipendenza dall'ordine dei test.
- [x] ID duplicato gestito secondo una regola esplicita nel README.
- [x] PostgreSQL reale utilizzato durante l'esecuzione, non sostituito da H2.

### 10.6 Sottotask di implementazione

- [x] Completare il gate Podman/PostgreSQL/Dev Services.
- [x] Creare progetto con messaging, Mutiny, Hibernate Reactive e driver PostgreSQL.
- [x] Creare evento, entità, processor e resource dello scheletro.
- [x] Configurare canali in-memory nei test senza dichiarare connettori inesistenti come `smallrye-in-memory`.
- [x] Scrivere test di persistenza e output.
- [x] Scrivere test specifici per ack dopo commit e mancato ack in caso di errore.
- [x] Scrivere il README con canali, eventi, stati e strategia di fallimento.
- [x] Verificare il rosso iniziale con PostgreSQL reale.
- [x] Implementare e verificare la soluzione locale.
- [x] Ripetere i test per escludere race condition asincrone.

### 10.7 Esiti

- Gate Podman: Dev Services funziona con `DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock` e `TESTCONTAINERS_RYUK_DISABLED=true`; l'immagine `postgres:14` è stata scaricata e usata realmente.
- Rosso: 4 test su 4 falliti.
- Verde: 4 test su 4, suite ripetuta più volte.
- Gli handler di Reactive Messaging girano su un contesto Vert.x duplicato ma non marcato sicuro: `Panache` fallisce con `requires a safe (isolated) Vert.x sub-context`. Serve `VertxContextSafetyToggle.setContextSafe(...)` più `Panache.withSession(...)`; `@Blocking` non è ammesso sui metodi che ritornano `Uni`.
- Un `nack` con eccezione chiude il canale in-memory (`SRMSG00028`), rendendo i test successivi dipendenti dall'ordine: il contratto definisce lo scarto come conferma senza pubblicazione, così il canale resta vivo.
- `deliveries` su `/audit` rende osservabile che un duplicato non produce un secondo messaggio, senza leggere il database dal thread di test.

---

## 11. Documentazione del repository da aggiornare

### 11.1 README principale

- [x] Aggiungere `scenarios/` al diagramma della struttura.
- [x] Spiegare la differenza tra `labs/` e `scenarios/`.
- [x] Aggiungere una tabella con i cinque scenari, domini e stato.
- [x] Indicare che gli scenari non contengono guide passo-passo.
- [x] Documentare i prerequisiti aggiuntivi Podman/PostgreSQL per lo scenario 05.
- [x] Documentare il carattere manuale del deploy OpenShift dello scenario 04.

### 11.2 `AGENTS.md`

Aggiungere o aggiornare:

- [x] distinzione tra esercizi guidati e scenari avanzati;
- [x] struttura `scenarios/NN-<feature>/`;
- [x] convenzioni `artifactId`, package e branch soluzione;
- [x] regola che vieta commenti guida e `TODO` negli scenari;
- [x] protocollo obbligatorio rosso sullo scheletro e verde sulla soluzione locale;
- [x] catalogo dei cinque scenari e relativi stati;
- [x] nota sulle verifiche OpenShift locali rispetto al deploy manuale;
- [x] nota su PostgreSQL/Podman per lo scenario reattivo.

### 11.3 Stati del catalogo

Usare gli stessi stati del repository:

- `pianificato`: presente solo nel piano;
- `traccia`: scheletro e test rossi verificati;
- `traccia + soluzione`: soluzione locale verde verificata.

Non segnare `traccia + soluzione` senza avere eseguito realmente i test.

---

## 12. Sequenza operativa completa

### Fase A — Baseline e primo scenario

- [x] Confermare che `main` contiene solo le tracce.
- [x] Creare lo scaffold `scenarios/01-payment-gateway`.
- [x] Completare README, codice e test dello scenario 01.
- [x] Verificare rosso e verde secondo il protocollo.
- [x] Correggere i problemi di formato riscontrati (`@Table` per parola riservata, ordine del ritardo nello stub, nome esplicito del circuito).

**Stima:** 90–120 minuti. **Esito:** completato, 9 test rossi sulla traccia e verdi sulla soluzione locale `sol/scenario-01-payment-gateway`.

### Fase B — Scenari REST/sicurezza/configurazione

- [x] Implementare `02-patient-records` con verifica rosso/verde.
- [x] Implementare `03-subscription-engine` con verifica rosso/verde.
- [x] Controllare che i due progetti non condividano stato o configurazioni accidentali.

**Stima:** 2–3 ore complessive. **Esito:** completata, 9 test rossi/verdi per lo scenario 02 e 8 per lo scenario 03.

### Fase C — Gate infrastrutturale e messaging reattivo

- [x] Eseguire il gate Podman/PostgreSQL.
- [x] Implementare `05-reactive-pipeline`.
- [x] Verificare acknowledgment e persistenza reale.
- [x] Ripetere i test asincroni.

**Stima:** 90–150 minuti, esclusi eventuali problemi di rete nel pull delle immagini. **Esito:** completata, gate Podman superato, 4 test rossi/verdi.

### Fase D — Health e OpenShift

- [x] Verificare property Quarkus 3.8.1.
- [x] Implementare `04-inventory-openshift`.
- [x] Verificare test health, manifest e immagine container.
- [x] Mantenere il deploy Minishift/CRC fuori dalle verifiche automatiche.

**Stima:** 90–120 minuti. **Esito:** completata, 7 test rossi/verdi, manifest e immagine verificati.

### Fase E — Documentazione e controllo finale

- [x] Aggiornare README principale.
- [x] Aggiornare `AGENTS.md`.
- [x] Aggiornare gli stati in questo piano.
- [x] Controllare `git status` e `git branch` per assicurare che nessuna soluzione sia finita su `main`.
- [x] Eseguire un controllo finale dei cinque README rispetto ai test.
- [x] Riepilogare per ogni scenario l'esito reale dei comandi eseguiti.

**Stima:** 45–60 minuti. **Esito:** completata.

**Stima complessiva:** 8–12 ore, includendo le soluzioni locali e le verifiche reali. Il lavoro svolto è dentro questa forbice; la parte più onerosa è stata la messa a punto di Hibernate Reactive in Reactive Messaging e del setup Podman.

---

## 13. Rischi e contromisure

| Rischio | Contromisura |
|---|---|
| Test rossi perché la traccia è rotta, non perché manca la soluzione | Verificare compilazione, avvio e causa di ogni assertion fallita |
| Test impossibili o incompatibili con Quarkus 3.8.1 | Implementare sempre una soluzione locale verde prima di accettare la traccia |
| Retry e timeout rendono i test lenti o intermittenti | Downstream controllabile, timeout di attesa espliciti, reset tra test e ripetizione della suite |
| Lettura del circuit breaker non allineata alla policy | Usare il nome FT supportato da SmallRye e verificare il comportamento a runtime |
| Metrica dei `403` non osservabile nel punto scelto | Validare subito `SecurityIdentity` e mapper/filter con un test dedicato |
| Baggage contaminato tra richieste | Test con due utenti consecutivi e chiusura corretta degli scope nella soluzione |
| Property OpenShift copiate da versioni recenti | Consultare la configurazione specifica 3.8.1 prima del README |
| Minishift non compatibile o non installato | Nessuna dipendenza dei test dal cluster; appendice manuale e CRC raccomandato |
| Dev Services non rileva Podman rootless | Configurare il socket oppure usare PostgreSQL esplicito via Podman |
| Acknowledgment verificato solo indirettamente | Usare un `Message` controllabile e asserire il completamento di ack/nack |
| Soluzioni pubblicate accidentalmente | Branch locali `sol/scenario-*`, mai pushati, controllo finale di `main` |

---

## 14. Definition of Done complessiva

Il lavoro è concluso quando tutte le condizioni seguenti sono vere:

- [x] Esistono cinque progetti sotto `scenarios/`.
- [x] Ogni progetto compila sulla traccia.
- [x] Ogni progetto ha test inizialmente rossi per i requisiti documentati.
- [x] Ogni progetto ha una soluzione locale verde verificata.
- [x] Nessun branch soluzione è stato pushato.
- [x] Nessun Java degli scheletri contiene commenti guida o `TODO`.
- [x] Nessun README degli scenari contiene una soluzione passo-passo.
- [x] Tutti i codici HTTP e le property richieste sono specificati.
- [x] Gli health check e i manifest OpenShift sono stati verificati localmente.
- [x] Lo scenario reattivo usa PostgreSQL reale tramite Podman.
- [x] I test non dipendono dall'ordine e quelli asincroni sono stati ripetuti.
- [x] README principale e `AGENTS.md` descrivono la nuova famiglia di esercizi.
- [x] Gli stati del catalogo riflettono esclusivamente verifiche effettivamente eseguite.

---

## 15. Prossima azione operativa

Creare soltanto `scenarios/01-payment-gateway/`, completarne traccia e test, quindi eseguire il primo `./mvnw test` e registrare l'esito reale prima di procedere con la soluzione locale.
