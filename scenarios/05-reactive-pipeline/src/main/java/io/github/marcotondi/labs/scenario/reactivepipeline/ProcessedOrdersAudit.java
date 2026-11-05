package io.github.marcotondi.labs.scenario.reactivepipeline;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Verifica che il messaggio di output arrivi solo dopo la persistenza.
 *
 * NON modificare.
 */
@ApplicationScoped
@Path("/audit")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessedOrdersAudit {

    private final Map<String, Boolean> persisted = new ConcurrentHashMap<>();
    private final Map<String, Integer> deliveries = new ConcurrentHashMap<>();

    @Incoming("orders-processed")
    public Uni<Void> consume(TransactionEvent event) {
        if (Vertx.currentContext() != null) {
            VertxContextSafetyToggle.setContextSafe(Vertx.currentContext(), true);
        }
        return Panache.withSession(
                        () -> Transaction.<Transaction>find("transactionId", event.transactionId).firstResult())
                .map(transaction -> {
                    deliveries.merge(event.transactionId, 1, Integer::sum);
                    persisted.put(event.transactionId, transaction != null);
                    return (Void) null;
                });
    }

    @GET
    @Path("/{transactionId}")
    public Response get(@PathParam("transactionId") String transactionId) {
        Integer count = deliveries.get(transactionId);
        if (count == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(Map.of(
                "transactionId", transactionId,
                "deliveries", count,
                "persistedWhenReceived", persisted.get(transactionId))).build();
    }
}
