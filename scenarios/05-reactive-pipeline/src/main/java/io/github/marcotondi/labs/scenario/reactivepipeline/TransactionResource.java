package io.github.marcotondi.labs.scenario.reactivepipeline;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @POST
    public Response submit(TransactionEvent event) {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/{transactionId}")
    public Response find(@PathParam("transactionId") String transactionId) {
        throw new UnsupportedOperationException();
    }
}
