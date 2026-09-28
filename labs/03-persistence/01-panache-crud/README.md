# Laboratorio 03 — Persistenza con Hibernate ORM e Panache

## Scenario

Il catalogo prodotti di un e-commerce va salvato su database. Il team ha
già scritto l'entità `Product` (Panache Active Record), il seeder di
avvio e la configurazione del database H2 in-memory. Manca solo la
logica REST, che deve usare **Panache** per leggere e scrivere.

Come sempre: il progetto compila, ma i test falliscono finché il task non
è completato.

## Requisiti (comportamento atteso)

| Metodo | Path | Atteso |
|--------|------|--------|
| GET    | `/api/products` | `200` + lista di tutti i prodotti |
| GET    | `/api/products/{id}` | `200` + prodotto, oppure `404` se non esiste |
| GET    | `/api/products/search?name=...` | `200` + lista filtrata per nome, **case-insensitive** |
| POST   | `/api/products` | `201 Created` + header `Location` + prodotto persistito |
| PUT    | `/api/products/{id}` | `200` + prodotto aggiornato, oppure `404` |
| DELETE | `/api/products/{id}` | `204 No Content`, oppure `404` |

I dati di partenza (seed) sono tre prodotti: `Mechanical Keyboard`,
`Wireless Mouse`, `USB-C Hub`.

## Istruzioni passo-passo

1. **Esplora**: leggi `Product` (estende `PanacheEntity`), `DataSeeder`
   (esempio d'uso di `persist` dentro una transazione) e
   `application.properties` (H2 in-memory già configurato). I file marcati
   "NON modificare" sono completi.
2. **Completa `ProductResource.java`** — ci sono 6 `TODO`:
   - `list()`: `Product.listAll()`.
   - `search(name)`: query Panache case-insensitive, es. con
     `Product.list("lower(name) like ?1", "%" + name.toLowerCase() + "%")`.
   - `get(id)`: `Product.findById(id)`; se `null` lancia
     `new jakarta.ws.rs.NotFoundException()`.
   - `create(product)`: `@Transactional`, `product.persist()`, risposta
     `201` con `Location` e body del prodotto creato.
   - `update(id, product)`: `@Transactional`, trova l'entità, aggiorna i
     campi, risposta `200` (o `404`).
   - `delete(id)`: `@Transactional`, trova l'entità, `delete()`, `204`
     (o `404`).
3. **Verifica** che i test dell'esaminatore passino.

## Comandi

```bash
# Avvio in modalità dev (H2 in-memory + seed automatico)
./mvnw quarkus:dev        # http://localhost:8080/api/products

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

Prova rapida con `curl`:

```bash
curl -i http://localhost:8080/api/products
curl -i -X POST -H "Content-Type: application/json" \
     -d '{"name":"Monitor 27","price":199.00,"stock":5}' \
     http://localhost:8080/api/products
```

## Regole del laboratorio

- Modifica **solo** `ProductResource.java`.
- Vietato aggiungere dipendenze o toccare i file marcati "NON modificare".
- Il database è H2 in-memory: nessun servizio esterno da avviare.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 9 i casi superati.
- **Correttezza Panache/JPA** (30%): uso dell'Active Record,
  `@Transactional` solo sui metodi di scrittura, `404` corretti, nessuna
  query costruita per concatenazione di stringhe (parametri `?1`).

Quando hai finito, mandami la tua soluzione (`ProductResource.java`): la
correggo come un esaminatore, indico se i test passano e ti do
punteggio + feedback.