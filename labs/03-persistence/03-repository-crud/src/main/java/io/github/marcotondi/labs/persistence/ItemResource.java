package io.github.marcotondi.labs.persistence;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: implementa il CRUD usando il repository (NON i metodi
 * Active Record, che su Item non esistono).
 */
@Path("/api/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @Inject
    ItemRepository repository;

    @GET
    public List<Item> list() {
        // TODO: Implementare qui -> repository.listAll()
        return null;
    }

    @GET
    @Path("/search")
    public List<Item> search(@QueryParam("name") String name) {
        // TODO: Implementare qui -> repository.findByName(name)
        return null;
    }

    @GET
    @Path("/in-stock")
    public List<Item> inStock() {
        // TODO: Implementare qui -> repository.inStock()
        return null;
    }

    @GET
    @Path("/{id}")
    public Item get(@PathParam("id") Long id) {
        // TODO: Implementare qui -> repository.findByIdOptional(id)
        // se vuoto -> jakarta.ws.rs.NotFoundException
        return null;
    }

    @POST
    @Transactional
    public Response create(Item item) {
        // TODO: Implementare qui
        // repository.persist(item); rispondi 201 + Location
        return null;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Item item) {
        // TODO: Implementare qui
        // repository.findByIdOptional(id); se vuoto -> 404
        // aggiorna i campi; rispondi 200 con l'entità
        return null;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        // TODO: Implementare qui
        // repository.deleteById(id): se false -> 404, altrimenti 204
        return null;
    }
}