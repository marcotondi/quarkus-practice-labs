# Esercizio 02-03 — Bean Validation

## Scenario

L'endpoint di creazione clienti accetta qualsiasi payload. Devi
applicare la **Jakarta Bean Validation** per rifiutare input non validi
con `400 Bad Request`.

## Regole di validazione

| Campo | Vincolo |
|-------|---------|
| `name` | non blank, lunghezza 2..50 |
| `email` | non blank, email valida |
| `age` | 18..120 |
| `loyaltyCode` | formato `AAA-999` (3 lettere maiuscole, `-`, 3 cifre) |

## Istruzioni passo-passo

1. **Esplora**: `Customer` (da completare), `CustomerResource` (da
   completare) e `CustomerValidationTest`.
2. **Completa `Customer.java`** con le annotazioni:
   - `name`: `@NotBlank`, `@Size(min = 2, max = 50)`;
   - `email`: `@NotBlank`, `@Email`;
   - `age`: `@Min(18)`, `@Max(120)`;
   - `loyaltyCode`: `@Pattern(regexp = "[A-Z]{3}-\\d{3}")`.
3. **Completa `CustomerResource.java`**: aggiungi `@Valid` al parametro
   di `create`.
4. **Verifica** che i test dell'esaminatore passino.

Le annotazioni stanno in `jakarta.validation.constraints.*` e
`jakarta.validation.Valid`.

## Comandi

```bash
./mvnw quarkus:dev
curl -i -X POST -H "Content-Type: application/json" \
  -d '{"name":"","email":"x","age":10,"loyaltyCode":"abc"}' \
  http://localhost:8080/api/customers

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `Customer.java` e `CustomerResource.java`.
- Vietato aggiungere dipendenze.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 6 i casi superati.
- **Correttezza** (30%): annotazioni appropriate per ogni regola,
  `@Valid` presente, nessuna validazione scritta a mano.

Quando hai finito, mandami la tua soluzione (`Customer.java` +
`CustomerResource.java`): la correggo come un esaminatore.