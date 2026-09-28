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
└── 02-rest-api/               # argomento: NN-<topic>
    └── 01-crud-api/           # esercizio: EE-<feature> (progetto Maven)
        ├── README.md          # il task del laboratorio
        ├── pom.xml
        └── src/
```

Convenzioni e decisioni complete: vedi [AGENTS.md](AGENTS.md).

## Indice laboratori

| Argomento | Esercizio | Stato |
|-----------|-----------|-------|
| 01-configuration | [01-typed-config](labs/01-configuration/01-typed-config/README.md) | disponibile |
| 01-configuration | 02-overrides-configsource | pianificato |
| 02-rest-api | [01-crud-api](labs/02-rest-api/01-crud-api/README.md) | disponibile |
| 02-rest-api | 02-exception-mapping | pianificato |
| 03-persistence | [01-panache-crud](labs/03-persistence/01-panache-crud/README.md) | disponibile |
| 03-persistence | 02-repository-relations | pianificato |
| 04-fault-tolerance | [01-resilience-policies](labs/04-fault-tolerance/01-resilience-policies/README.md) | disponibile |
| 04-fault-tolerance | 02-bulkhead-async | pianificato |
| 05-health | [01-liveness-readiness](labs/05-health/01-liveness-readiness/README.md) | disponibile |
| 06-security | [01-jwt-rbac](labs/06-security/01-jwt-rbac/README.md) | disponibile |
| 06-security | 02-oidc | pianificato |
| 07-metrics | [01-counters-timers](labs/07-metrics/01-counters-timers/README.md) | disponibile |
| 08-openapi | [01-api-documentation](labs/08-openapi/01-api-documentation/README.md) | disponibile |
| 09-rest-client | 01-declarative-client | pianificato |
| 10-messaging | 01-pipelines-ack | pianificato |

## Come usare un laboratorio

```bash
# 1. entra nel progetto della traccia
cd labs/02-rest-api/01-crud-api

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
