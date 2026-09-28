# Esercizio 04-02 — Bulkhead e Asynchronous

## Scenario

Un servizio elabora richieste pesanti. Se arrivano troppe chiamate
contemporaneamente, il sistema va in sovraccarico. Devi:

- eseguire il lavoro **in modo asincrono** (`@Asynchronous`);
- limitare la **concorrenza** con un bulkhead (`@Bulkhead`);
- rispondere con un **fallback** alle chiamate rifiutate (`@Fallback`).

## Comportamento atteso

| Metodo | Requisito |
|--------|-----------|
| `threadName()` | con `@Asynchronous` gira su un thread diverso dal chiamante |
| `process(id)` | `@Asynchronous` + `@Bulkhead(value = 1, waitingTaskQueue = 1)` + `@Fallback` |
| chiamata rifiutata | ritorna `rejected:<id>` (fallback) |

## Istruzioni passo-passo

1. **Esplora**: `LoadService` (da completare) e `Gate` (controllo di
   sincronizzazione usato dai test, completa).
2. **Completa `LoadService.java`**:
   - `threadName()`: annota con `@Asynchronous`;
   - `process(id)`: annota con `@Asynchronous`,
     `@Bulkhead(value = 1, waitingTaskQueue = 1)` e
     `@Fallback(fallbackMethod = "rejected")`.
3. **Verifica** che i test dell'esaminatore passino.

Le annotazioni stanno in `org.eclipse.microprofile.faulttolerance.*`.

## Comandi

```bash
# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `LoadService.java`.
- Vietato aggiungere dipendenze o toccare `Gate`.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 2 i casi superati.
- **Correttezza FT** (30%): `@Asynchronous` + `@Bulkhead` combinati
  correttamente, `waitingTaskQueue = 1`, fallback con la stessa firma.

Quando hai finito, mandami la tua soluzione (`LoadService.java`): la
correggo come un esaminatore.