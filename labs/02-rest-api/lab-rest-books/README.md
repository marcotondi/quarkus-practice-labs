# Laboratorio 02 — REST API (JAX-RS / RESTEasy Reactive)

## Scenario

La tua azienda sta costruendo il backend di una biblioteca digitale.
Il team ha già scritto l'entità `Book`, il repository in-memory
`BookRepository` e i DTO validati `BookDTO`. Manca solo la parte REST:
il file **`BookResource.java`** è incompleto e i test dell'esaminatore
falliscono.

Come nell'esame reale: **non si parte da zero**, il codice compila, ma
comportamento atteso e test falliscono finché non completi il task.

## Comportamento atteso (requisiti esaminatore)

| Metodo | Path            | Atteso                                                        |
|--------|-----------------|---------------------------------------------------------------|
| GET    | `/api/books`    | `200` + array JSON di DTO (mai l'entità interna)              |
| GET    | `/api/books/{id}` | `200` + DTO oppure `404` se il libro non esiste             |
| POST   | `/api/books`    | `201 Created` + header `Location: /api/books/{id}` + DTO con id |
| POST   | `/api/books`    | `400` se il payload viola la validazione (title vuoto, year mancante, ...) |

## Istruzioni passo-passo

1. **Esplora il progetto**: leggi `Book`, `BookDTO`, `BookRepository`,
   `BookNotFoundException` e `BookNotFoundExceptionMapper`. Sono tutti
   completi: NON modificarli.
2. **Completa `BookResource.java`** — ci sono 4 `// TODO: Implementare qui`:
   - `list()`: mappa ogni `Book` in `BookDTO` e restituisci la lista.
   - `get(id)`: cerca nel repository; se assente lancia
     `new BookNotFoundException(id)` (il mapper risponderà `404`).
   - `create(dto)`: aggiungi `@Valid` al parametro, costruisci il `Book`,
     salvalo, rispondi `201` con `Location` e body del DTO creato.
   - `toDTO(book)`: riempi `id`, `title`, `author`, `year`.
3. **Verifica** che i test passino.
4. (Facoltativo) Prova a mano con `curl`:
   ```bash
   curl -i http://localhost:8080/api/books
   curl -i -X POST -H "Content-Type: application/json" \
        -d '{"title":"Dune","author":"Frank Herbert","year":1965}' \
        http://localhost:8080/api/books
   ```

## Comandi

```bash
# Avvio in modalità dev (hot reload)
./mvnw quarkus:dev

# Esecuzione dei test dell'esaminatore (OBBLIGATORIO: devono passare TUTTI)
./mvnw test
```

Se volessi rigenerare il progetto da zero:

```bash
mvn io.quarkus.platform:quarkus-maven-plugin:3.8.1:create \
  -DprojectGroupId=com.redhat.ex378 -DprojectArtifactId=lab-rest-books \
  -DclassName=com.redhat.ex378.rest.BookResource -Dpath=/api/books \
  -Dextensions=resteasy-reactive-jackson,hibernate-validator
```

## Regole d'esame

- Vietato aggiungere dipendenze, framework o librerie (niente Lombok).
- Vietato modificare i file contrassegnati "NON modificare".
- Il body del `404` è libero: conta lo status code.
- Niente eccezioni "sporcate": usa gli strumenti che il progetto ti dà
  (`BookNotFoundException`, mapper, validazione via `@Valid`).

## Criteri di valutazione (rubrica esaminatore)

- **Test** (60%): tutti e 6 i casi superati.
- **Correttezza REST** (20%): status code corretti (`201`+`Location`,
  `404`, `400`), nessun `500` spurio.
- **Best practice** (20%): separazione entità/DTO rispettata, nessun
  code smell (es. catch & swallow, `new Response(...)` mutabile,
  mapping manuale ripetuto dove esiste già `toDTO`).

Quando hai finito, mandami la tua soluzione (`BookResource.java` e
quant'altro hai modificato): la correggo come un esaminatore, indico se
i test passano e ti do punteggio + feedback.