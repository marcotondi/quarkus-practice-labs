package io.github.marcotondi.labs.scenario.reactivepipeline;

import io.smallrye.mutiny.Uni;
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

    private final Map<String, Boolean> received = new ConcurrentHashMap<>();

    @Incoming("orders-processed")
    public Uni<Void> consume(TransactionEvent event) {
        return Transaction.<Transaction>find("transactionId", event.transactionId).firstResult()
                .map(transaction -> {
                    received.put(event.transactionId, transaction != null);
                    return (Void) null;
                });
    }

    @GET
    @Path("/{transactionId}")
    public Response get(@PathParam("transactionId") String transactionId) {
        Boolean persisted = received.get(transactionId);
        if (persisted == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(Map.of(
                "transactionId", transactionId,
                "persistedWhenReceived", persisted)).build();
    }
}
