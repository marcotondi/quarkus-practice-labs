# Esercizio 01-03 — Configurazione gerarchica con @ConfigMapping

## Scenario

La configurazione del server ha una struttura **gerarchica**:

```
server.host
server.port
server.limits.max-connections
server.limits.timeout-seconds
```

Devi mapparla su un'interfaccia **`@ConfigMapping`** con un **gruppo
annidato**, invece di iniettare singole `@ConfigProperty`.

## Comportamento atteso

`GET /api/server-config` deve restituire:

| Campo | Valore | Origine |
|-------|--------|---------|
| `host` | `localhost` | `server.host` |
| `port` | `9090` | `server.port` |
| `maxConnections` | `25` | `server.limits.max-connections` |
| `timeoutSeconds` | `30` | default del mapping (`@WithDefault`) |

## Istruzioni passo-passo

1. **Esplora**: `ServerConfigResource` (da completare),
   `application.properties` e `ServerInfo` (DTO, completa).
2. **Crea `ServerConfig`** (`io.github.marcotondi.labs.configuration`):
   ```java
   @ConfigMapping(prefix = "server")
   public interface ServerConfig {
       String host();
       int port();
       Limits limits();
       interface Limits {
           int maxConnections();
           @WithDefault("30")
           int timeoutSeconds();
       }
   }
   ```
3. **Aggiungi la proprietà mancante** in `application.properties`:
   `server.limits.max-connections=25`.
4. **Inietta `ServerConfig`** nel resource e implementa `info()`.
5. **Verifica** che il test dell'esaminatore passi.

Nota: il nome del metodo `maxConnections()` corrisponde alla chiave
`max-connections` (kebab-case).

## Comandi

```bash
./mvnw quarkus:dev
curl http://localhost:8080/api/server-config

# Test dell'esaminatore (deve passare)
./mvnw test
```

## Regole del laboratorio

- Crea `ServerConfig`, modifica `ServerConfigResource` e
  `application.properties`. Non toccare `ServerInfo`.
- Vietato aggiungere dipendenze.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): il caso superato.
- **Correttezza** (30%): `@ConfigMapping` con gruppo annidato,
  `@WithDefault` usato dove serve, proprietà gerarchica corretta.

Quando hai finito, mandami la tua soluzione (`ServerConfig.java` +
`ServerConfigResource.java` + `application.properties`): la correggo come
un esaminatore.