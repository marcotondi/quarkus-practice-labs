# Laboratorio 06 — Sicurezza JWT (RBAC)

## Scenario

Un'API espone dati pubblici e aree riservate. L'autenticazione avviene
con **JWT firmati** (RS256): la chiave pubblica è già configurata in
`application.properties` (`publicKey.pem`). Manca il **controllo di
accesso basato sui ruoli**: oggi chiunque può chiamare ogni endpoint.

## Comportamento atteso

| Endpoint | Accesso | Atteso |
|----------|---------|--------|
| `GET /api/public` | aperto | `200` |
| `GET /api/user` | ruolo **user** | senza token `401`; con ruolo user `200` |
| `GET /api/admin` | ruolo **admin** | con ruolo user `403`; con ruolo admin `200` |
| `GET /api/me` | ruolo **user** | `200` + `{"username": <preferred_username>, "roles": [...]}` |

I ruoli arrivano dal claim `groups` del token.

## Istruzioni passo-passo

1. **Esplora**: `SecuredResource` (il file da completare),
   `application.properties` (verifica JWT già configurata) e
   `SecuredResourceTest` (i test generano token reali con la chiave
   privata di test).
2. **Completa `SecuredResource.java`** — ci sono 3 `TODO`:
   - annota `userEndpoint()` con `@RolesAllowed("user")`;
   - annota `adminEndpoint()` con `@RolesAllowed("admin")`;
   - implementa `me()`: richiede ruolo `user` e restituisce il JSON con
     `username` (dal claim `preferred_username`) e `roles` (i `groups`).
     Inietta `org.eclipse.microprofile.jwt.JsonWebToken`.
3. **Verifica** che i test dell'esaminatore passino.

## Comandi

```bash
# Avvio in modalità dev
./mvnw quarkus:dev

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `SecuredResource.java`.
- Vietato aggiungere dipendenze o modificare la configurazione JWT.
- Le chiavi in `src/main/resources/publicKey.pem` e
  `src/test/resources/privateKey.pem` sono **di test**: non usarle in
  produzione.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 6 i casi superati.
- **Correttezza sicurezza** (30%): `@RolesAllowed` corretto su ogni
  endpoint, `/api/public` lasciato aperto, lettura del token via
  `JsonWebToken`, nessun controllo di ruolo scritto a mano.

Quando hai finito, mandami la tua soluzione (`SecuredResource.java`): la
correggo come un esaminatore e ti do punteggio + feedback.