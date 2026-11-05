# Scenario 04 — Inventory Tracking with OpenShift & Health Groups

## Scenario

Un servizio di tracciamento magazzino deve essere distribuito su
OpenShift. La piattaforma deve poter capire da sola se il processo è vivo
e se può ricevere traffico. Il team operativo vuole inoltre un endpoint
di diagnostica approfondita separato dalle sonde usate da OpenShift, e
l'URL del database deve arrivare da una `ConfigMap`, non dal codice.

Il progetto compila e si avvia in locale, ma gli health check non
riflettono lo stato reale, il gruppo di diagnostica non esiste e non è
presente alcuna configurazione OpenShift.

## Obiettivi EX378 coperti

- Custom Health Check MicroProfile con `@Liveness` e `@Readiness`.
- SmallRye Health con `data(...)` e gruppi `@HealthGroup`.
- Configurazione dell'estensione `quarkus-openshift`: Route, probe,
  variabili da `ConfigMap`.
- Profili di configurazione e separazione tra ambiente di test e
  deployment.

## Requisiti tecnici dettagliati

### Health check

| Endpoint | Controllo | Nome | Dati obbligatori |
|---|---|---|---|
| `/q/health/live` | memoria JVM libera | `inventory-memory` | `freeMemoryBytes` |
| `/q/health/ready` | connessione al database | `inventory-database` | `database` = `reachable` |
| `/q/health/group/deep-checks` | entrambi i controlli | — | — |

Regole:

- La liveness è `DOWN` quando la memoria libera stimata scende sotto la
  property `inventory.health.min-free-memory-bytes` (byte, default
  `1048576`).
- `freeMemoryBytes` riporta la memoria libera stimata come numero.
- La readiness è `DOWN` se la connessione al database non è valida;
  `database` vale `reachable` solo quando la connessione è stata
  verificata con successo.
- Entrambi i controlli appartengono al gruppo `deep-checks`.
- Gli endpoint health non richiedono autenticazione.

### Configurazione OpenShift

Property richieste, con questi nomi esatti:

```properties
quarkus.openshift.route.expose=true
quarkus.openshift.liveness-probe.http-path=/q/health/live
quarkus.openshift.readiness-probe.http-path=/q/health/ready
quarkus.openshift.env.configmaps=inventory-config
```

La `ConfigMap` si chiama `inventory-config` e deve esistere come manifest
in `src/main/kubernetes/openshift.yml`, con chiave
`QUARKUS_DATASOURCE_JDBC_URL` contenente l'URL JDBC PostgreSQL. Nessuna
credenziale reale nel repository.

Il profilo `prod` deve usare PostgreSQL; i test usano H2 per restare
deterministici. Il driver PostgreSQL è già tra le dipendenze.

### Persistenza

- Entità `StockItem`, tabella `stock_items`, usata dal servizio per
  rendere reale il controllo database.
- Endpoint `GET /stock` già fornito.

## Istruzioni di verifica

```bash
./mvnw test
```

Generazione del manifest OpenShift e ispezione:

```bash
./mvnw package
grep -n "kind: Route" target/kubernetes/openshift.yml
grep -n "kind: ConfigMap" target/kubernetes/openshift.yml
grep -n "livenessProbe\|readinessProbe" target/kubernetes/openshift.yml
```

Build dell'immagine container con Podman:

```bash
export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock
./mvnw package -Dquarkus.container-image.build=true
```

Avvio locale:

```bash
./mvnw quarkus:dev
curl -s http://localhost:8080/q/health/live
curl -s http://localhost:8080/q/health/ready
curl -s http://localhost:8080/q/health/group/deep-checks
```

## Deploy su OpenShift (esercizio manuale, non verificato dai test)

I test verificano health check, configurazione e build. Non verificano il
deploy.

Minishift è associato a OKD 3.11, è obsoleto e non è raccomandato per
Quarkus 3.8. L'alternativa supportata è OpenShift Local (CRC) o un
cluster OpenShift 4. Esempio, con `oc` già autenticato:

```bash
./mvnw package -Dquarkus.kubernetes.deploy=true
oc get route inventory
```

## Regole del laboratorio

- Modificare solo codice in `src/main`.
- Non modificare i test.
- Non aggiungere dipendenze.
- Non introdurre credenziali o URL di database nel codice Java.
- Non disattivare gli health check.
- I test usano profili diversi e non dipendono dall'ordine di
  esecuzione.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (60%): tutti i casi superati.
- **Correttezza del dominio** (20%): soglia memoria da configurazione,
  dati degli health check, gruppo `deep-checks`, property OpenShift con
  nomi esatti, `ConfigMap` coerente.
- **Best practice** (20%): nessun `System.out`, nessuna credenziale nel
  repository, risorse chiuse correttamente, nessun import inutilizzato.
