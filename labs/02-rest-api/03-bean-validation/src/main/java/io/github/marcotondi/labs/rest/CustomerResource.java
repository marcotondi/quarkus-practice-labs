package io.github.marcotondi.labs.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio.
 */
@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    // TODO: Implementare qui
    // Attiva la validazione sul parametro con @Valid.
    @POST
    public Response create(Customer customer) {
        customer.id = 1L;
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }
}