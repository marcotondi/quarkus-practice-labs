# Esercizio 10-01 — Pipeline reattiva e acknowledgment

## Scenario

Un servizio ordini deve elaborare i messaggi in modo **asincrono**:
l'ordine entra su un canale, viene trasformato e raccolto da un altro
consumer. Devi collegare i canali con **SmallRye Reactive Messaging**,
usando canali **in-memory** (nessun Kafka o connettore esterno).

## Comportamento atteso

| Azione | Atteso |
|--------|--------|
| `POST /api/orders/ORD-1` | `202 Accepted` (il messaggio entra nel canale `orders-in`) |
| elaborazione | il messaggio diventa `processed:ORD-1` e viene raccolto da `orders-out` |
| `GET /api/orders` | contiene `processed:ORD-1` |

## Istruzioni passo-passo

1. **Esplora**: `OrderProcessor` (da completare), `OrderResource`
   (espone l'emitter sul canale `orders-in`, completa) e
   `application.properties`. I canali sono in-memory: emitter e
   `@Incoming` con lo stesso nome sono collegati automaticamente.
2. **Completa `OrderProcessor.java`**:
   - `transform`: ha già `@Incoming("orders-in")`; aggiungi
     `@Outgoing("orders-out")`;
   - `collect`: aggiungi `@Incoming("orders-out")` (riceve un
     `Message<String>` e lo conferma con `message.ack()`).
3. **Verifica** che i test dell'esaminatore passino.

Le annotazioni stanno in `org.eclipse.microprofile.reactive.messaging.*`.
L'acknowledgment è il meccanismo con cui il consumer conferma di aver
elaborato il messaggio: `message.ack()`.

## Comandi

```bash
./mvnw quarkus:dev
curl -X POST http://localhost:8080/api/orders/ORD-1
curl http://localhost:8080/api/orders

# Test dell'esaminatore (devono passare TUTTI)
./mvnw test
```

## Regole del laboratorio

- Modifica **solo** `OrderProcessor.java`.
- Vietato aggiungere dipendenze o toccare `OrderResource` e la config.

## Criteri di valutazione (rubrica esaminatore)

- **Test** (70%): il caso superato.
- **Correttezza** (30%): `@Incoming`/`@Outgoing` corretti, canali
  coerenti con la configurazione, acknowledgment esplicito.

Quando hai finito, mandami la tua soluzione (`OrderProcessor.java`): la
correggo come un esaminatore.