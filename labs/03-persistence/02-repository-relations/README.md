# Esercizio 03-02 — Repository pattern e relazioni

## Scenario

La libreria deve gestire **autori** con i loro **libri**. Le entità con la
relazione `@OneToMany` / `@ManyToOne` sono già definite; devi implementare
l'accesso ai dati con il pattern **`PanacheRepository`** e gli endpoint.

## Comportamento atteso

| Metodo | Endpoint | Atteso |
|--------|----------|--------|
| `POST` | `/api/authors` | `201` + `Location`; salva autore **e libri** (cascade) |
| `GET` | `/api/authors` | `200` + lista autori con i titoli dei libri |
| `GET` | `/api/authors/{id}` | `200` + autore con libri, oppure `404` |
| `GET` | `/api/authors/by-name/{name}` | `200` + autore con libri, oppure `404` |

## Istruzioni passo-passo

1. **Esplora**: `Author` e `Book` (relazione bidirezionale, complete),
   `AuthorRepository` e `AuthorResource` (da completare).
2. **Completa `AuthorRepository.java`** (`PanacheRepository`):
   - `findByName`: `find("select distinct a from Author a left join fetch a.books where a.name = ?1", name).firstResultOptional()`
   - `findByIdWithBooks`: stessa query con `where a.id = ?1`
   - `listWithBooks`: `find("select distinct a from Author a left join fetch a.books").list()`
3. **Completa `AuthorResource.java`**:
   - `create`: crea `Author`, chiama `addBook` per ogni titolo,
     `repository.persist(author)`, rispondi `201` + `Location`;
   - `list`/`get`/`byName`: usa il repository e mappa con `toResponse`;
   - `get`/`byName`: se assente lancia `jakarta.ws.rs.NotFoundException`;
   - `toResponse`: mappa `id`, `name` e i titoli dei libri.
4. **Verifica** che i test dell'esaminatore passino.

Il `left join fetch` è importante: senza, i libri (lazy) non sarebbero
disponibili fuori dalla transazione.

## Comandi

```bash
./mvnw quarkus:dev
curl http://localhost:8080/api/authors

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `AuthorRepository.java` e `AuthorResource.java`.
- Vietato aggiungere dipendenze o toccare entità e DTO.
- H2 in-memory: nessun servizio esterno.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 4 i casi superati.
- **Correttezza** (30%): pattern `PanacheRepository`, `left join fetch`
  (niente lazy fuori transazione), cascade corretto, `404` corretti.

Quando hai finito, mandami la tua soluzione (`AuthorRepository.java` +
`AuthorResource.java`): la correggo come un esaminatore.