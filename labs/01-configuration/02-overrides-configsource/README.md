# Esercizio 01-02 — Override di configurazione e ConfigSource custom

## Scenario

L'applicazione legge due impostazioni: `app.mode` (con un default) e
`app.team`. Il valore di `app.team` deve arrivare da una **sorgente di
configurazione aziendale** (un `ConfigSource` custom) che ha priorità più
alta del file `application.properties`.

## Comportamento atteso

`GET /api/settings` deve restituire:

| Campo | Valore | Da dove |
|-------|--------|---------|
| `mode` | `standard` | `@ConfigProperty` con `defaultValue` |
| `mode` (con override di test) | `overridden` | override di configurazione |
| `team` | `platform` | `ConfigSource` custom (ordinal 300 > application.properties) |

## Istruzioni passo-passo

1. **Esplora**: `AppSettings`, `SettingsResource`, `application.properties`
   (`app.team=default`) e `SettingsTest`.
2. **Completa `AppSettings.java`**:
   - inietta `app.mode` con
     `@ConfigProperty(name = "app.mode", defaultValue = "standard")`;
   - inietta `app.team` con `@ConfigProperty(name = "app.team")`.
3. **Crea il `ConfigSource` custom** `TeamConfigSource`
   (package `io.github.marcotondi.labs.configuration`):
   - implementa `org.eclipse.microprofile.config.spi.ConfigSource`;
   - `getValue("app.team")` → `"platform"`, `getOrdinal()` → `300`,
     `getName()` → `"team-config-source"`.
4. **Registra il `ConfigSource`** creando il file
   `src/main/resources/META-INF/services/org.eclipse.microprofile.config.spi.ConfigSource`
   con dentro il nome completo della classe.
5. **Verifica** che i test dell'esaminatore passino.

## Comandi

```bash
./mvnw quarkus:dev
curl http://localhost:8080/api/settings

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica `AppSettings.java` e crea `TeamConfigSource` + il file di
  registrazione. Non toccare `SettingsResource` e `Settings`.
- Vietato aggiungere dipendenze.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 3 i casi superati.
- **Correttezza** (30%): `@ConfigProperty` con default corretto,
  `ConfigSource` registrato via ServiceLoader, ordinal coerente.

Quando hai finito, mandami la tua soluzione (`AppSettings.java` +
`TeamConfigSource.java`): la correggo come un esaminatore.