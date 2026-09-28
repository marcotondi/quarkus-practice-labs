# Quarkus Practice Labs

Raccolta di laboratori pratici in stile esame di certificazione per lo
sviluppo cloud-native con Quarkus. Ogni laboratorio è una traccia
realistica: un progetto Quarkus parzialmente funzionante con casi di test
JUnit che **falliscono** finché il task non è completato.

Questo repo contiene **solo le tracce** (il codice di partenza). Le
soluzioni non sono pubblicate.

## Requisiti

- JDK **17+** (testato su Temurin 21)
- Maven 3.9+ (o il wrapper `./mvnw` incluso nel progetto)
- Un IDE con autocompletamento (VS Codium / IntelliJ): non serve
  ricordare gli import a memoria

Baseline: **Quarkus 3.8.x** (pin `3.8.1`), Java 17, Maven.

## Struttura

```
labs/
└── 02-rest-api/               # cartella lab: NN-<slug>
    └── lab-rest-books/        # progetto Maven della traccia
        ├── README.md          # il task del laboratorio
        ├── pom.xml
        └── src/
```

Convenzioni e decisioni complete: vedi [AGENTS.md](AGENTS.md).

## Indice laboratori

| N. | Lab | Argomento | Stato |
|----|-----|-----------|-------|
| 01 | [01-config-profiles](labs/01-config-profiles/lab-catalog-config/README.md) | Configurazione e profili (`%dev`/`%test`/`%prod`) | traccia disponibile |
| 02 | [02-rest-api](labs/02-rest-api/lab-rest-books/README.md) | REST API JAX-RS: DTO, validation, status code | traccia disponibile |
| 03 | [03-persistence-panache](labs/03-persistence-panache/lab-product-catalog/README.md) | Hibernate ORM + Panache (CRUD, query, transazioni) | traccia disponibile |
| 04 | `04-fault-tolerance` | MicroProfile Fault Tolerance | pianificato |
| 05 | `05-health-check` | SmallRye Health | pianificato |
| 06 | `06-security-jwt` | SmallRye JWT, RBAC | pianificato |

## Come usare un laboratorio

```bash
# 1. entra nel progetto della traccia
cd labs/02-rest-api/lab-rest-books

# 2. leggi il task
cat README.md

# 3. verifica che i test falliscano (è lo stato di partenza)
./mvnw test

# 4. lavora sul codice, poi riprova finché tutti i test passano
./mvnw test

# 5. opzionale: avvia l'app in modalità dev
./mvnw quarkus:dev     # http://localhost:8080
```

## Come resettare una traccia

Dopo aver lavorato, per tornare al codice di partenza:

```bash
git restore .
```

## Nota legale

Materiale didattico **originale**, creato per esercitazione. Non è
materiale d'esame reale, non riproduce domande d'esame e non è
affiliato, approvato o sponsorizzato da Red Hat. `Red Hat` ed `EX378`
sono marchi dei rispettivi proprietari, citati solo a scopo descrittivo.

## Licenza

Distribuito con licenza [MIT](LICENSE).

## Aggiungere un nuovo laboratorio

1. Crea `labs/NN-<slug>/<progetto-maven>/`.
2. Scrivi la traccia seguendo la checklist in [AGENTS.md](AGENTS.md) §5.
3. Verifica che `./mvnw test` fallisca sul codice di partenza.
4. Aggiorna l'indice in questo README e il catalogo in AGENTS.md.
5. Commit su `main` (solo la traccia, mai la soluzione).
