# AGENTS.md — Convenzioni e decisioni del repo EX378 Labs

Fonte di verità per chiunque (umano o agente AI) lavori in questo repo.
Se una regola qui contraddice il codice, si aggiorna il codice senza
chiedere.

---

## 1. Scopo del repo

Simulatore di laboratori d'esame per la certificazione **EX378 — Red Hat
Certified Specialist in Cloud-native Development**. Ogni laboratorio è una
"traccia" d'esame: un progetto Quarkus parzialmente funzionante + test
JUnit che falliscono, con un task da completare.

Il repo pubblicato su GitHub contiene **solo le tracce**. Le soluzioni
restano in locale (vedi §4). Il materiale è originale e non riproduce
contenuti d'esame reali. Il nome del repo pubblico non deve usare i
marchi della certificazione (es. `EX378`): usare un nome neutro tipo
`quarkus-practice-labs`. Il README riporta la nota legale.

Licenza: **MIT** (file `LICENSE`).

---

## 2. Baseline tecnico

| Voce | Valore |
|---|---|
| Red Hat Build of Quarkus | **3.8.x** — pin a `3.8.1` |
| Java | target **17** (`maven.compiler.release=17`), gira su JDK 21 |
| Build | Maven 3.9+ |
| Group id | `io.github.marcotondi.labs` |
| Package | `io.github.marcotondi.labs.<dominio>` |

> **Nota storica**: Quarkus non ha mai rilasciato una `3.8.0.Final`
> (esisteva solo `3.8.0.CR1`). La prima finale della linea 3.8 è la
> **3.8.1**, che corrisponde alla Red Hat Build of Quarkus 3.8. Ogni
> riferimento a "3.8.0.Final" va tradotto in `3.8.1`.
> In 3.8 l'estensione REST è ancora `quarkus-resteasy-reactive` /
> `quarkus-resteasy-reactive-jackson` (il rename a `quarkus-rest` arriva
> in 3.9+).

Ogni progetto include il **Maven wrapper**: si usa `./mvnw` e non serve
Maven installato sulla macchina. I comandi documentati usano `./mvnw`.

---

## 3. Struttura e naming convention

```
quarkus-ex378/                     # root repo — su GitHub
├── README.md                      # guida per chi usa i lab
├── AGENTS.md                      # questo file
├── .gitignore
└── labs/
    └── NN-<topic>/                # argomento
        ├── EE-<feature>/          # esercizio (progetto Maven) — 3-4 feature
        │   ├── pom.xml
        │   ├── README.md          # LA TRACCIA (task d'esame)
        │   └── src/
        └── EE-<altra-feature>/    # altro esercizio dello stesso argomento
```

Regole di naming:
- **Argomento**: `NN-<topic>` — due cifre, kebab-case, sostantivo breve.
  Esempi: `01-configuration`, `02-rest-api`, `07-metrics`.
- **Esercizio**: `EE-<feature>` — due cifre + slug kebab-case.
  Più esercizi = più cartelle sorelle dentro l'argomento. Ogni esercizio
  è un progetto Maven autonomo (traccia + test) che copre 3-4 feature.
  Esempi: `01-typed-config`, `02-overrides-configsource`.
- **`artifactId` Maven**: `lab-<feature>` (es. `lab-typed-config`).
- **Package Java**: `io.github.marcotondi.labs.<topic>`
  (es. `io.github.marcotondi.labs.configuration`).
- **Branch soluzione**: `sol/EE-<feature>` (es. `sol/01-typed-config`).

Vietato usare spazi o maiuscole nei nomi di cartella (rompe path e URL).

---

## 4. Workflow tracce / soluzioni

- `main` contiene **solo tracce**: il codice di partenza con i `TODO` e i
  test che falliscono. È il branch che si pusha su GitHub.
- Le soluzioni vivono su branch **locali** `sol/EE-<feature>`, che **non si
  pushano mai**. Il comando sicuro di pubblicazione è:
  ```bash
  git push origin main
  ```
- Per resettare una traccia dopo averla sporcata:
  ```bash
  git restore .
  ```
- Per iniziare un nuovo lab: `git switch -c sol/EE-<feature>` dal `main`,
  risolvere, committare sul branch, poi tornare su `main`.

Motivo: i colleghi ricevono le tracce pulite; le soluzioni non escono
mai dalla macchina.

---

## 5. Anatomia di una traccia (checklist)

Ogni laboratorio **deve** contenere:

1. **`README.md`** con: scenario, comportamento atteso (tabella endpoint
   o requisiti), istruzioni passo-passo, comandi, regole d'esame,
   criteri di valutazione.
2. **Progetto Maven completo** che compila e si avvia.
3. **Codice di partenza parziale**, con commenti `// TODO: Implementare
   qui`, che compila ma non soddisfa i requisiti. I file non da
   modificare sono marcati "NON modificare" e restano completi.
4. **Test JUnit** (`@QuarkusTest`) dell'esaminatore che **devono
   fallire** sul codice di partenza. Requisito verificato: prima di
   pubblicare una traccia, eseguire `./mvnw test` e confermare che i test
   falliscano.
5. **Comandi** di avvio (`./mvnw quarkus:dev`) e test (`./mvnw test`).

Regole d'esame da rispettare nelle tracce:
- Niente dipendenze extra, niente Lombok, niente framework aggiunti dal
  candidato.
- I test non devono dipendere dall'ordine di esecuzione tra loro.

---

## 6. Catalogo argomenti ed esercizi

| Argomento | Esercizio | Stato |
|-----------|-----------|-------|
| 01-configuration | 01-typed-config | **traccia + soluzione** |
| 01-configuration | 02-overrides-configsource | traccia |
| 02-rest-api | 01-crud-api | **traccia + soluzione** |
| 02-rest-api | 02-exception-mapping | traccia |
| 03-persistence | 01-panache-crud | traccia |
| 03-persistence | 02-repository-relations | traccia |
| 04-fault-tolerance | 01-resilience-policies | traccia |
| 04-fault-tolerance | 02-bulkhead-async | pianificato |
| 05-health | 01-liveness-readiness | traccia |
| 06-security | 01-jwt-rbac | traccia |
| 06-security | 02-oidc | pianificato |
| 07-metrics | 01-counters-timers | traccia |
| 08-openapi | 01-api-documentation | traccia |
| 09-rest-client | 01-declarative-client | pianificato |
| 10-messaging | 01-pipelines-ack | pianificato |

Stato possibili: `pianificato` → `traccia` → `traccia + soluzione`.

---

## 7. Protocollo dell'esaminatore (correzione)

Quando il candidato invia una soluzione:

1. **Eseguire davvero i test**: `./mvnw test` nel progetto del lab. Riportare
   l'esito reale (non simulato).
2. **Diagnosi onesta**: se un test fallisce, stabilire se la colpa è del
   codice del candidato, della traccia, o di un comportamento del
   framework. Se il test dell'esaminatore è troppo rigido, correggerlo e
   dirlo (es. cap. §8).
3. **Valutare le best practice**: separazione entità/DTO, gestione
   corretta degli errori, assenza di output di debug (`System.out`),
   import inutilizzati, commenti obsoleti rimasti.
4. **Punteggio**: `Superato` / `Quasi` / `Non superato`, con motivazione e
   riferimenti alla documentazione ufficiale.
5. **Feedback costruttivo e specifico**, con la riga di codice da
   cambiare.

Criteri quantitativi usati nel Lab 02 (rubrica di riferimento):
test 60%, correttezza REST 20%, best practice 20%.

---

## 8. Trappole note (lezioni apprese)

- **`Location` e URI relativi in RESTEasy Reactive**: passare un `URI`
  relativo a `Response.created(...)` fa sì che il framework lo risolva
  contro l'URL della richiesta → in risposta arriva un `Location`
  **assoluto** (`http://host:port/api/books/3`). È comportamento
  spec-compliant, non un bug. Quindi:
  - lato codice: costruire il `Location` con
    `@Context UriInfo` + `uriInfo.getAbsolutePathBuilder().path(id)`;
  - lato test: asserire con `endsWith("/api/books/" + id)`, non con
    `startsWith("/api/books/")`.
- **Non esporre mai l'entità interna**: la POST deve restituire il DTO,
  non l'oggetto di dominio, anche quando i nomi dei campi coincidono
  (coincidenza ≠ correttezza; in produzione l'entità ha campi interni).
- **`@Valid` è obbligatorio** sul parametro del metodo REST, altrimenti le
  annotazioni di validazione sul DTO restano inattive e le richieste
  invalide passano con `201`.
