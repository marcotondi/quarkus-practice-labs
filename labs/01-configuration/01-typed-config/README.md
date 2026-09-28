# Laboratorio 01 — Configurazione e Profili

## Scenario

Il servizio `catalog` viene distribuito in tre ambienti: sviluppo, test e
produzione. Oggi, però, tutti usano la **stessa** configurazione:
`application.properties` contiene valori unici, validi per qualunque
ambiente. Il risultato è che in test ci si connette al database di
sviluppo e in produzione si mostrano messaggi sbagliati.

Devi introdurre la configurazione **per profilo**.

## Configurazione attesa

L'applicazione espone `GET /api/catalog/info`, che restituisce la
configurazione risolta dal profilo attivo. I valori attesi sono:

| Proprietà                    | `%dev`            | `%test`            | `%prod`                                    |
|------------------------------|-------------------|--------------------|--------------------------------------------|
| `catalog.greeting`           | `Ciao (dev)`      | `Hello from test`  | `Hello`                                    |
| `catalog.items-per-page`     | `10`              | `5`                | `50`                                       |
| `catalog.datasource-url`     | `jdbc:h2:mem:devdb` | `jdbc:h2:mem:testdb` | `jdbc:postgresql://prod-db:5432/catalog` |
| `catalog.new-ui-enabled`     | `true`            | `false`            | `true`                                     |

## Istruzioni passo-passo

1. **Esplora**: apri `CatalogConfig` (mappatura tipizzata già pronta) e
   `application.properties` (configurazione attuale, uguale per tutti).
   I file marcati "NON modificare" sono completi.
2. **Modifica solo `application.properties`**: aggiungi gli override per
   `%dev`, `%test` e `%prod` in modo che i valori risolti corrispondano
   alla tabella. I valori di base possono restare come default.
3. **Verifica** che i test dell'esaminatore passino.

Nota sulla sintassi: un override di profilo si scrive
`%<profilo>.<chiave>=<valore>`, per esempio
`%prod.catalog.items-per-page=50`.

## Comandi

```bash
# Avvio in modalità dev (profilo %dev)
./mvnw quarkus:dev        # http://localhost:8080/api/catalog/info

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

Per provare un profilo a mano:

```bash
./mvnw quarkus:dev -Dquarkus.profile=prod
```

## Regole del laboratorio

- Modifica **solo** `application.properties`.
- Vietato aggiungere dipendenze o toccare i file marcati "NON modificare".
- I test usano `@TestProfile` per attivare `dev`, `test` e `prod`.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 3 i casi superati (test, dev, prod).
- **Correttezza** (30%): uso corretto della sintassi `%profilo.chiave`,
  nessun valore duplicato inutilmente, default sensati lasciati alla base.

Quando hai finito, mandami la tua soluzione (`application.properties`):
la correggo come un esaminatore, indico se i test passano e ti do
punteggio + feedback.