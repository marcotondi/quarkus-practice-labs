# Laboratorio 05 — Health Check (SmallRye Health)

## Scenario

Il servizio `inventory` monitora una dipendenza (`DownstreamService`) che
può essere disponibile o meno. Il team di piattaforma ha bisogno di due
health check:

- una **liveness** sempre `UP` (l'applicazione è viva);
- una **readiness** che segue lo stato della dipendenza: `UP` quando è
  disponibile, `DOWN` quando non lo è.

Oggi non esiste nessun health check personalizzato: gli endpoint
rispondono `UP` a prescindere.

## Comportamento atteso

| Endpoint | Atteso |
|----------|--------|
| `GET /q/health/live` | `200`, status `UP`, presente il check chiamato **`app`** (liveness) |
| `GET /q/health/ready` | `200`, status `UP`, presente il check chiamato **`downstream`** (readiness) |
| `GET /q/health/ready` (dipendenza giù) | `503`, status `DOWN` |

## Istruzioni passo-passo

1. **Esplora**: leggi `DownstreamService` (la dipendenza monitorata,
   completa) e il `README`. Non c'è codice da modificare: devi **creare**
   i due health check.
2. **Crea `AppLivenessCheck`** nel package
   `io.github.marcotondi.labs.health`:
   - annotata `@Liveness` e `@ApplicationScoped`;
   - implementa `org.eclipse.microprofile.health.HealthCheck`;
   - `call()` restituisce `HealthCheckResponse.up("app")`.
3. **Crea `DownstreamHealthCheck`** nello stesso package:
   - annotata `@Readiness` e `@ApplicationScoped`;
   - inietta `DownstreamService` e restituisce
     `HealthCheckResponse.up("downstream")` se la dipendenza è su,
     `HealthCheckResponse.down("downstream")` altrimenti.
4. **Verifica** che i test dell'esaminatore passino.

## Comandi

```bash
# Avvio in modalità dev
./mvnw quarkus:dev

# Verifica a mano
curl -s http://localhost:8080/q/health/live
curl -s http://localhost:8080/q/health/ready

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Non modificare `DownstreamService`.
- Vietato aggiungere dipendenze.
- I nomi dei check (`app`, `downstream`) sono vincolanti: fanno parte del
  contratto verificato dai test.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 3 i casi superati.
- **Correttezza** (30%): uso corretto di `@Liveness`/`@Readiness`,
  distinzione liveness/readiness rispettata, nome dei check corretto.

Quando hai finito, mandami la tua soluzione (i file `AppLivenessCheck.java`
e `DownstreamHealthCheck.java`): la correggo come un esaminatore e ti do
punteggio + feedback.