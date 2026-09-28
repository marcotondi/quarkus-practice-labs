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
    └── NN-<slug>/                 # un lab = una cartella
        └── <progetto-maven>/      # uno o più progetti Maven del lab
            ├── pom.xml
            ├── README.md          # LA TRACCIA (task d'esame)
            └── src/
```

Regole di naming:
- **Cartella lab**: `NN-<slug>` — due cifre, kebab-case.
  Esempi: `01-config-profiles`, `02-rest-api`, `03-persistence-panache`.
- **Progetto Maven dentro il lab**: kebab-case senza numero.
  Esempi: `lab-rest-books`, `lab-greetings-api`.
  Se un lab richiede più progetti, si mettono più cartelle sorelle
  dentro `NN-<slug>/`.
- **`artifactId` Maven** di un progetto traccia: `lab-<slug-progetto>`.
- **Branch soluzione**: `sol/NN-<slug>`.

Vietato usare spazi o maiuscole nei nomi di cartella (rompe path e URL).

---

## 4. Workflow tracce / soluzioni

- `main` contiene **solo tracce**: il codice di partenza con i `TODO` e i
  test che falliscono. È il branch che si pusha su GitHub.
- Le soluzioni vivono su branch **locali** `sol/NN-<slug>`, che **non si
  pushano mai**. Il comando sicuro di pubblicazione è:
  ```bash
  git push origin main
  ```
- Per resettare una traccia dopo averla sporcata:
  ```bash
  git restore .
  ```
- Per iniziare un nuovo lab: `git switch -c sol/NN-<slug>` dal `main`,
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

## 6. Catalogo laboratori

| N. | Slug | Argomento | Stato |
|----|------|-----------|-------|
| 01 | `config-profiles` | Configurazione e profili (`%dev`/`%test`/`%prod`) | **traccia + soluzione** |
| 02 | `rest-api` | REST API JAX-RS, DTO, validation, status code | **traccia + soluzione** |
| 03 | `persistence-panache` | Hibernate ORM + Panache | traccia |
| 04 | `fault-tolerance` | MicroProfile Fault Tolerance (`@Retry`, `@Timeout`, `@CircuitBreaker`, `@Fallback`) | traccia |
| 05 | `health-check` | SmallRye Health (liveness/readiness) | pianificato |
| 06 | `security-jwt` | SmallRye JWT, RBAC (`@RolesAllowed`) | pianificato |

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
