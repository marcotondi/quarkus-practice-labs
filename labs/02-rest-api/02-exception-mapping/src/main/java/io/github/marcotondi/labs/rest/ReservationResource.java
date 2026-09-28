package io.github.marcotondi.labs.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: gestisci gli errori con eccezioni + ExceptionMapper.
 */
@Path("/api/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

    @Inject
    ReservationRepository repository;

    @GET
    @Path("/{id}")
    public Reservation get(@PathParam("id") Long id) {
        // TODO: Implementare qui
        // repository.findById(id); se null -> lancia
        // new ReservationNotFoundException(id)
        return null;
    }

    @POST
    public Response create(Reservation reservation) {
        // TODO: Implementare qui
        // 1. attiva la validazione (@Valid sul parametro)
        // 2. se repository.findByCode(...) != null -> lancia
        //    new ReservationConflictException(code)
        // 3. salva e rispondi 201 Created con Location
        return null;
    }
}