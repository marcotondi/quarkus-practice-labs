# Esercizio 09-01 — Rest Client dichiarativo

## Scenario

Il catalogo deve chiamare un **servizio remoto** "widgets". Devi
implementare un **MicroProfile Rest Client**: un'interfaccia Java che
diventa automaticamente un client HTTP, con URL preso dalla
configurazione e un header custom.

## Comportamento atteso

| Endpoint locale | Atteso |
|-----------------|--------|
| `GET /api/catalog/widget/1` | `200` + `{"id":1,"name":"Gadget"}` dal servizio remoto |
| `GET /api/catalog/header` | `200` + `{"echo":"quarkus-labs"}` (header `X-Client` inviato dal client) |

## Istruzioni passo-passo

1. **Esplora**: `WidgetClient` (interfaccia da completare),
   `CatalogResource` (da completare), `DownstreamTestServer` (server di
   test, completa) e `application.properties`
   (`quarkus.rest-client.widgets-api.url`).
2. **Completa `WidgetClient.java`**: aggiungi a entrambi i metodi
   `@ClientHeaderParam(name = "X-Client", value = "quarkus-labs")`
   (i metodi hanno già `@GET`/`@Path`).
3. **Completa `CatalogResource.java`**:
   - `widget`: `return client.getWidget(id);`
   - `header`: `return client.header();`
4. **Verifica** che i test dell'esaminatore passino.

La `configKey = "widgets-api"` dell'interfaccia è legata alla proprietà
`quarkus.rest-client.widgets-api.url`.

## Comandi

```bash
./mvnw quarkus:dev
curl http://localhost:8080/api/catalog/widget/1

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `WidgetClient.java` e `CatalogResource.java`.
- Vietato aggiungere dipendenze o toccare il server di test.
- L'URL del servizio remoto **non** va hardcoded: usa la configurazione.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 2 i casi superati.
- **Correttezza** (30%): `@RegisterRestClient` con `configKey`, URL da
  configurazione, `@ClientHeaderParam` corretto, nessun URL hardcoded.

Quando hai finito, mandami la tua soluzione (`WidgetClient.java` +
`CatalogResource.java`): la correggo come un esaminatore.