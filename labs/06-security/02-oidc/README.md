# Esercizio 06-02 — Sicurezza OIDC (RBAC)

## Scenario

Un'API è protetta tramite **OpenID Connect**. Devi applicare il controllo
di accesso basato sui ruoli (`@Authenticated`, `@RolesAllowed`) e
restituire l'identità dell'utente autenticato.

## Comportamento atteso

| Endpoint | Accesso | Atteso |
|----------|---------|--------|
| `GET /api/public` | aperto | `200` |
| `GET /api/me` | autenticato | senza identità `401`; con identità `200` + nome utente |
| `GET /api/admin` | ruolo **admin** | con ruolo user `403`; con ruolo admin `200` |

## Istruzioni passo-passo

1. **Esplora**: `AccountResource` (da completare) e `AccountResourceTest`
   (usa `@TestSecurity` + `@OidcSecurity` per simulare l'identità OIDC).
2. **Completa `AccountResource.java`**:
   - `me()`: annota con `@Authenticated` e ritorna
     `identity.getPrincipal().getName()`;
   - `admin()`: annota con `@RolesAllowed("admin")`.
3. **Verifica** che i test dell'esaminatore passino.

Le annotazioni: `io.quarkus.security.Authenticated` e
`jakarta.annotation.security.RolesAllowed`.

## Comandi

```bash
./mvnw quarkus:dev

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `AccountResource.java`.
- Vietato aggiungere dipendenze o modificare la configurazione OIDC.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): tutti e 5 i casi superati.
- **Correttezza sicurezza** (30%): `@Authenticated`/`@RolesAllowed`
  corretti, `/api/public` lasciato aperto, lettura dell'identità via
  `SecurityIdentity`, nessun controllo di ruolo scritto a mano.

Quando hai finito, mandami la tua soluzione (`AccountResource.java`): la
correggo come un esaminatore.