# EX378 Labs — Simulatore d'esame Red Hat Quarkus

Raccolta di laboratori in stile **EX378 — Red Hat Certified Specialist in
Cloud-native Development**. Ogni laboratorio è una traccia d'esame
realistica: un progetto Quarkus parzialmente funzionante con casi di test
JUnit che **falliscono** finché il task non è completato.

Questo repo contiene **solo le tracce** (il codice di partenza). Le
soluzioni non sono pubblicate.

## Requisiti

- JDK **17+** (testato su Temurin 21)
- Maven 3.9+
- Un IDE con autocompletamento (VS Codium / IntelliJ) — come all'esame,
  non serve ricordare gli import a memoria

Baseline: **Red Hat Build of Quarkus 3.8.x** (pin `3.8.1`), Java 17,
Maven, package `com.redhat.ex378`.

## Struttura

```
labs/
└── 02-rest-api/               # cartella lab: NN-<slug>
    └── lab-rest-books/        # progetto Maven della traccia
        ├── README.md          # il task d'esame
        ├── pom.xml
        └── src/
```

Convenzioni e decisioni complete: vedi [AGENTS.md](AGENTS.md).

## Indice laboratori

| N. | Lab | Argomento | Stato |
|----|-----|-----------|-------|
| 02 | [02-rest-api](labs/02-rest-api/lab-rest-books/README.md) | REST API JAX-RS: DTO, validation, status code | traccia disponibile |
| 01 | `01-config-profiles` | Configurazione e profili | pianificato |
| 03 | `03-persistence-panache` | Hibernate ORM + Panache | pianificato |
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
mvn test

# 4. lavora sul codice, poi riprova finché tutti i test passano
mvn test

# 5. opzionale: avvia l'app in modalità dev
mvn quarkus:dev     # http://localhost:8080
```

## Come resettare una traccia

Dopo aver lavorato, per tornare al codice di partenza:

```bash
git restore .
```

## Aggiungere un nuovo laboratorio

1. Crea `labs/NN-<slug>/<progetto-maven>/`.
2. Scrivi la traccia seguendo la checklist in [AGENTS.md](AGENTS.md) §5.
3. Verifica che `mvn test` fallisca sul codice di partenza.
4. Aggiorna l'indice in questo README e il catalogo in AGENTS.md.
5. Commit su `main` (solo la traccia, mai la soluzione).
