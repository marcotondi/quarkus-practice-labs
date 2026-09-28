# Esercizio 09-02 — ResponseExceptionMapper (REST Client)

## Scenario

Il servizio remoto "widgets" risponde `404` quando un widget non esiste.
Il client REST, di default, lancia un'eccezione generica. Devi
intercettare la risposta d'errore con un **`ResponseExceptionMapper`** e
convertirla in un'eccezione di dominio, poi gestirla nel resource locale.

## Comportamento atteso

| Endpoint locale | Atteso |
|-----------------|--------|
| `GET /api/catalog/widget/1` | `200` + `{"id":1,"name":"Gadget"}` |
| `GET /api/catalog/widget/999` | `404` + `{"code":"not_found","message":"..."}` |

## Istruzioni passo-passo

1. **Esplora**: `WidgetClient` (da completare), `CatalogResource` (da
   completare), `WidgetNotFoundException`, `ErrorResponse`,
   `DownstreamTestServer` (server di test, completa).
2. **Crea `WidgetNotFoundExceptionMapper`**:
   ```java
   public class WidgetNotFoundExceptionMapper
           implements ResponseExceptionMapper<WidgetNotFoundException> {
       @Override
       public boolean handles(int status, MultivaluedMap<String, Object> headers) {
           return status == 404;
       }
       @Override
       public WidgetNotFoundException toThrowable(Response response) {
           return new WidgetNotFoundException(null);
       }
   }
   ```
3. **Registra il mapper** sull'interfaccia con
   `@RegisterProvider(WidgetNotFoundExceptionMapper.class)`.
4. **Completa `CatalogResource.widget`**: chiama il client e, se viene
   lanciata `WidgetNotFoundException`, rispondi `404` con
   `new ErrorResponse("not_found", e.getMessage())`.
5. **Verifica** che i test dell'esaminatore passino.

Le classi stanno in `org.eclipse.microprofile.rest.client.ext` /
`org.eclipse.microprofile.rest.client.annotation`.

## Comandi

```bash
./mvnw quarkus:dev

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica `WidgetClient.java` e `CatalogResource.java`, crea
  `WidgetNotFoundExceptionMapper`.
- Vietato aggiungere dipendenze o toccare il server di test.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 2 i casi superati.
- **Correttezza** (30%): `ResponseExceptionMapper` con `handles` sul
  `404`, registrato con `@RegisterProvider`, gestione dell'eccezione nel
  resource, URL da configurazione.

Quando hai finito, mandami la tua soluzione: la correggo come un
esaminatore.