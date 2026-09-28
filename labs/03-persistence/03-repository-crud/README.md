# Esercizio 03-03 — CRUD con PanacheRepository

## Scenario

Stesso dominio del CRUD Active Record, ma con il **pattern Repository**.
L'entità `Item` è una `@Entity` JPA **pura** (non estende `PanacheEntity`),
quindi i metodi Active Record non esistono: si usa `PanacheRepository`.

## Comportamento atteso

| Metodo | Endpoint | Atteso |
|--------|----------|--------|
| GET | `/api/items` | `200` + lista |
| GET | `/api/items/{id}` | `200` oppure `404` |
| GET | `/api/items/search?name=...` | `200` + risultati per nome |
| GET | `/api/items/in-stock` | `200` + solo elementi con `quantity > 0` |
| POST | `/api/items` | `201` + `Location` |
| PUT | `/api/items/{id}` | `200` oppure `404` |
| DELETE | `/api/items/{id}` | `204` oppure `404` |

Seed: `Mechanical Keyboard` (12), `Wireless Mouse` (0), `USB-C Hub` (7).

## Istruzioni passo-passo

1. **Esplora**: `Item` (entità pura), `ItemRepository` e `ItemResource`
   (da completare), `DataSeeder` (esempio d'uso del repository).
2. **Completa `ItemRepository.java`**:
   - `findByName`: `list("name", name)`
   - `inStock`: `list("quantity > 0")`
3. **Completa `ItemResource.java`** usando i metodi di `PanacheRepository`:
   - `list()` → `repository.listAll()`
   - `get(id)` → `repository.findByIdOptional(id).orElseThrow(NotFoundException::new)`
   - `create` → `repository.persist(item)` + `201`/`Location`
   - `update` → `findByIdOptional` + aggiorna i campi + `200`/`404`
   - `delete` → `repository.deleteById(id)`; `false` → `404`, altrimenti `204`
4. **Verifica** che i test dell'esaminatore passino.

Confronta con `03-persistence/01-panache-crud` (stesso CRUD con **Active
Record**) per vedere le differenze tra i due pattern.

## Comandi

```bash
./mvnw quarkus:dev
curl http://localhost:8080/api/items

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `ItemRepository.java` e `ItemResource.java`.
- Vietato usare i metodi Active Record (non esistono su `Item`).
- Vietato aggiungere dipendenze.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 8 i casi superati.
- **Correttezza** (30%): uso del pattern Repository, `@Transactional` solo
  sulle scritture, `404` corretti, query parametrizzate.

Quando hai finito, mandami la tua soluzione: la correggo come un
esaminatore.