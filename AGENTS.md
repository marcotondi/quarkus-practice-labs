# AGENTS.md — Convenzioni del repo (simulatore lab EX378)

Fonte di verità per umani e agenti. Se una regola contraddice il codice,
si aggiorna il codice.

## 1. Scopo

Simulatore di lab d'esame **EX378 — Red Hat Certified Specialist in
Cloud-native Development**: ogni lab è una **traccia** = progetto Quarkus
parziale + test che falliscono + task da completare. Su GitHub vanno
**solo le tracce** (§4). Materiale originale, non affiliato a Red Hat;
nome repo neutro (`quarkus-practice-labs`), nota legale nel README.
Licenza: **MIT**.

## 2. Baseline tecnico

- **Quarkus 3.8.1** — non esiste `3.8.0.Final` (solo `3.8.0.CR1`); la
  3.8.1 è la RHBQ 3.8. REST: `quarkus-resteasy-reactive[-jackson]` (il
  rename `quarkus-rest` arriva in 3.9+).
- **Java** target 17 (gira su JDK 21), **Maven** 3.9+ con wrapper incluso
  → usare sempre `./mvnw`.
- groupId `io.github.marcotondi.labs`; package
  `io.github.marcotondi.labs.<topic>`.

## 3. Struttura e naming

```
labs/
  NN-<topic>/              # argomento
    EE-<feature>/          # esercizio = progetto Maven (3-4 feature)
      pom.xml, README.md (la traccia), src/
```

- **Argomento**: `NN-<topic>` (es. `01-configuration`).
- **Esercizio**: `EE-<feature>` (es. `01-typed-config`); più esercizi =
  cartelle sorelle dentro l'argomento.
- `artifactId`: `lab-<feature>`. Package: `io.github.marcotondi.labs.<topic>`.
- **Branch soluzione**: `sol/EE-<feature>`.
- Vietati spazi/maiuscole nei nomi di cartella.

## 4. Workflow tracce / soluzioni

- `main` = **solo tracce** (`// TODO` + test rossi). Push: `git push origin main`.
- Soluzioni su branch **locali** `sol/EE-<feature>`, **mai pushati**.
- Reset di una traccia: `git restore .`.
- Nuovo lab: `git switch -c sol/EE-<feature>` dal `main`, risolvere,
  committare, tornare su `main`.

## 5. Checklist di una traccia

1. **`README.md`**: scenario, comportamento atteso, comandi, regole,
   rubrica. Il livello di guida è **progressivo** (vedi sotto).
2. Progetto Maven che compila e si avvia.
3. Codice parziale con `// TODO: Implementare qui`; i file marcati
   "NON modificare" restano completi.
4. Test `@QuarkusTest` che **devono fallire** sulla traccia. Verificato
   con `./mvnw test` prima del commit.
5. Comandi `./mvnw quarkus:dev` e `./mvnw test`.

Regole: niente dipendenze extra / Lombok / framework aggiunti dal
candidato; test indipendenti dall'ordine di esecuzione.

**Scaffold progressivo del README.** Il README serve a far *ragionare*, non a
dettare la soluzione. Solo il **primo** esercizio di un argomento (o dei primi
argomenti) può avere istruzioni passo-passo e snippet di codice pronti. Dagli
esercizi successivi in poi: niente elenco di passi, niente codice finale nel
README; restano scenario, comportamento atteso, vincoli e rubrica, e il
candidato deduce da sé classi, annotazioni e proprietà.

## 6. Catalogo (20 esercizi / 11 argomenti)

`*` = ha anche il branch soluzione.

- **01-configuration**: 01-typed-config\*, 02-overrides-configsource, 03-config-mapping
- **02-rest-api**: 01-crud-api\*, 02-exception-mapping, 03-bean-validation
- **03-persistence**: 01-panache-crud, 02-repository-relations, 03-repository-crud
- **04-fault-tolerance**: 01-resilience-policies, 02-bulkhead-async
- **05-health**: 01-liveness-readiness
- **06-security**: 01-jwt-rbac, 02-oidc
- **07-metrics**: 01-counters-timers
- **08-openapi**: 01-api-documentation
- **09-rest-client**: 01-declarative-client, 02-response-exception-mapper
- **10-messaging**: 01-pipelines-ack
- **11-observability**: 01-tracing-baggage

Stati: `pianificato` → `traccia` → `traccia + soluzione`.

## 7. Protocollo esaminatore

Su una soluzione ricevuta:

1. **Eseguire davvero** `./mvnw test`; riportare l'esito reale (non simulato).
2. **Diagnosi onesta**: colpa del codice, della traccia o del framework?
   Se il test è troppo rigido, correggerlo e dirlo (§8).
3. **Best practice**: separazione entità/DTO, gestione errori, niente
   `System.out`, niente import inutili / commenti obsoleti.
4. **Punteggio** `Superato` / `Quasi` / `Non superato` + motivazione + doc ufficiale.
5. **Feedback specifico**, con la riga di codice da cambiare.

Rubrica di riferimento (lab REST): test 60%, correttezza REST 20%,
best practice 20%.

## 8. Trappole note

- **`Location`**: `Response.created(uriRelativo)` → RESTEasy lo
  assolutizza (`http://host:port/...`). Codice:
  `uriInfo.getAbsolutePathBuilder().path(id)`; test: `endsWith("/.../" + id)`,
  non `startsWith`.
- **Mai esporre l'entità**: la POST restituisce il DTO, anche se i campi
  coincidono.
- **`@Valid` obbligatorio** sul parametro REST, altrimenti la validazione
  è inattiva e l'input invalido passa con `201`.
- **`@Bulkhead`**: `waitingTaskQueue` minimo **1** (0 è invalido).
- **Reactive Messaging**: il connettore `smallrye-in-memory` **non esiste**;
  i canali in-memory sono automatici. Un `Emitter` richiede un subscriber
  sul canale, altrimenti l'app non parte.
- **REST Client**: un'interfaccia senza metodi HTTP annotati non viene
  registrata → build fallisce.

## 9. Backlog: domini da rafforzare

Da coprire in esercizi futuri (stato attuale tra parentesi).

**Assenti — da creare:**
- **Mutiny avanzato**: `Uni.combine().all().unis(a, b).asTuple()`,
  `onItem().transform/transformToUni`, `onFailure().recoverWithItem/retry`.
- **Transazioni avanzate**: `@Transactional(TxType.REQUIRES_NEW)`,
  `QuarkusTransaction.requiringNew().run(...)`, `setRollbackOnly()`.
- **Dev Services / container**: PostgreSQL via Testcontainers nei test,
  disabilitazione Dev Services, datasource esplicito.
- **Test mocking**: `@InjectMock`, `@InjectSpy`.

**Parziali — da estendere:**
- **`@QuarkusTestResource`** (server HTTP in-process, `09-rest-client`):
  Testcontainers/WireMock + override di proprietà in `start()`.
- **REST Client** (`09-rest-client/01`, `/02`): header dinamici
  (`@HeaderParam`, `ClientHeadersFactory`), token, client `Uni<T>`.
- **Bean Validation** (`02-rest-api/03`): `ConstraintViolationException`
  + `ExceptionMapper` con errori custom (campo → messaggio).
- **OpenAPI** (`08-openapi/01`): `@OpenAPIDefinition`/`@Info`,
  `@APIResponses`, security scheme.

**Coperti — estensioni opzionali:**
- **`@ConfigMapping`** (`01-01`, `01-03`): gruppi profondi, `Optional`,
  `List`, `Map`.
- **Custom Health Check** (`05-health/01`): `data(...)`, `/q/health/group`.

Ordine suggerito: Mutiny → Transazioni → Dev Services → REST Client →
Validation mapper → Test mocking → OpenAPI.
