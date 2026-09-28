package io.github.marcotondi.labs.persistence;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio.
 */
@Path("/api/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @Inject
    AuthorRepository repository;

    @POST
    @Transactional
    public Response create(AuthorRequest request) {
        // TODO: Implementare qui
        // 1. crea un Author, imposta name
        // 2. per ogni titolo chiama author.addBook(title)
        // 3. repository.persist(author) (cascade salva anche i libri)
        // 4. rispondi 201 Created con Location e toResponse(author)
        return null;
    }

    @GET
    public List<AuthorResponse> list() {
        // TODO: Implementare qui -> repository.listWithBooks(), mappa con toResponse
        return null;
    }

    @GET
    @Path("/{id}")
    public AuthorResponse get(@PathParam("id") Long id) {
        // TODO: Implementare qui -> repository.findByIdWithBooks(id)
        // se vuoto -> jakarta.ws.rs.NotFoundException
        return null;
    }

    @GET
    @Path("/by-name/{name}")
    public AuthorResponse byName(@PathParam("name") String name) {
        // TODO: Implementare qui -> repository.findByName(name)
        // se vuoto -> jakarta.ws.rs.NotFoundException
        return null;
    }

    private AuthorResponse toResponse(Author author) {
        // TODO: Implementare qui -> id, name, bookTitles (author.books -> title)
        return null;
    }
}