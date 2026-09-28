# Esercizio 02-02 — Gestione degli errori (ExceptionMapper)

## Scenario

L'API prenotazioni risponde con errori incoerenti (o li lascia diventare
`500`). Devi introdurre una **gestione uniforme degli errori**: eccezioni
di dominio + `ExceptionMapper` che producono un corpo JSON standard.

## Comportamento atteso

| Caso | Status | Corpo |
|------|--------|-------|
| `GET /api/reservations/1` | `200` | prenotazione |
| `GET /api/reservations/999` | `404` | `{"code":"not_found","message":"..."}` |
| `POST` valida | `201` | prenotazione + `Location` |
| `POST` con `code` duplicato | `409` | `{"code":"conflict","message":"..."}` |
| `POST` non valida (code vuoto, seats 0) | `400` | (validazione) |

## Istruzioni passo-passo

1. **Esplora**: `ReservationResource` (da completare), `Reservation`,
   `ReservationRepository`, `ReservationNotFoundException`,
   `ReservationConflictException`, `ErrorResponse`.
2. **Completa `ReservationResource.java`**:
   - `get`: `findById`; se `null` lancia `ReservationNotFoundException`;
   - `create`: aggiungi `@Valid`, controlla `findByCode` e lancia
     `ReservationConflictException`, poi salva e rispondi `201` + `Location`.
3. **Crea i due `ExceptionMapper`** (`@Provider`):
   - `ReservationNotFoundExceptionMapper` → `404` con
     `new ErrorResponse("not_found", e.getMessage())`;
   - `ReservationConflictExceptionMapper` → `409` con
     `new ErrorResponse("conflict", e.getMessage())`.
4. **Verifica** che i test dell'esaminatore passino.

## Comandi

```bash
./mvnw quarkus:dev
curl -i http://localhost:8080/api/reservations/999

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica `ReservationResource.java` e crea i due `ExceptionMapper`.
- Vietato aggiungere dipendenze o toccare gli altri file.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 5 i casi superati.
- **Correttezza** (30%): status code corretti (`404`/`409`/`400`), corpo
  d'errore uniforme, `@Valid` presente, nessun `catch` che inghiotte.

Quando hai finito, mandami la tua soluzione (`ReservationResource.java` +
i due mapper): la correggo come un esaminatore.