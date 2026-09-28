# Esercizio 08-01 — Documentazione API (OpenAPI)

## Scenario

L'API `Widgets` non è documentata: il documento OpenAPI è generato ma
privo di descrizioni. Aggiungi le annotazioni **MicroProfile OpenAPI** per
renderla utilizzabile da chi la consuma (e da Swagger UI).

## Comportamento atteso

Nel documento OpenAPI (`GET /q/openapi`) devono comparire:

| Elemento | Testo atteso |
|----------|--------------|
| `summary` dell'operazione `listWidgets` | `Elenca i widget` |
| `operationId` | `listWidgets` |
| descrizione risposta `201` del `POST` | `Widget creato` |
| descrizione del campo `name` dello schema `Widget` | `Nome visualizzato del widget` |
| descrizione del tag | `Gestione widget` |

## Istruzioni passo-passo

1. **Esplora**: `WidgetResource`, `Widget` e `OpenApiTest`.
2. **Completa `Widget.java`** — ci sono `TODO`:
   - annota il campo `name` con
     `@Schema(description = "Nome visualizzato del widget", example = "Gadget")`.
3. **Completa `WidgetResource.java`** — `TODO`:
   - `@Tag(name = "Widgets", description = "Gestione widget")` sulla classe;
   - `@Operation(summary = "Elenca i widget", operationId = "listWidgets")` su `list()`;
   - `@Operation(summary = "Crea un widget")` e
     `@APIResponse(responseCode = "201", description = "Widget creato")` su `create()`.
4. **Verifica** che i test dell'esaminatore passino.

Le annotazioni stanno in `org.eclipse.microprofile.openapi.annotations.*`.

## Comandi

```bash
# Avvio in modalità dev
./mvnw quarkus:dev
curl http://localhost:8080/q/openapi
# Swagger UI: http://localhost:8080/q/swagger-ui

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `Widget.java` e `WidgetResource.java`.
- Vietato aggiungere dipendenze.
- I testi sono vincolanti: fanno parte del contratto verificato.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 5 i casi superati.
- **Correttezza** (30%): annotazioni appropriate, `operationId` stabile,
  descrizioni su operation, risposta e schema.

Quando hai finito, mandami la tua soluzione (`Widget.java` e
`WidgetResource.java`): la correggo come un esaminatore e ti do
punteggio + feedback.