package com.redhat.ex378.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST endpoint: /api/books
 *
 * ATTENZIONE: questo file è INCOMPLETO. I quattro TODO sottostanti
 * sono il task del laboratorio. Tutto il resto del progetto è
 * già funzionante.
 */
@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookRepository repository;

    @GET
    public List<BookDTO> list() {
        // TODO: Implementare qui
        // 1. Iterare su repository.findAll()
        // 2. Mappare ogni Book nel relativo BookDTO (usare toDTO)
        // 3. Restituire la lista di DTO
        return null;
    }

    @GET
    @Path("/{id}")
    public BookDTO get(@PathParam("id") Long id) {
        // TODO: Implementare qui
        // 1. Cercare il libro con repository.findById(id)
        // 2. Se non esiste: lanciare new BookNotFoundException(id)
        //    (il mapper esistente risponderà 404)
        // 3. Altrimenti mappare e restituire il DTO
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(BookDTO dto) {
        // TODO: Implementare qui
        // 1. Validare il DTO: aggiungi @Valid al parametro del metodo
        //    -> una violazione produce automaticamente un 400
        // 2. Costruire un nuovo Book dai campi di dto (l'id lo assegna
        //    repository.save)
        // 3. Salvare con repository.save(book)
        // 4. Rispondere con 201 Created, header "Location" pari a
        //    "/api/books/{id}" e body = DTO del libro creato (id incluso)
        return null;
    }

    /**
     * Converte l'entità interna in DTO esposto.
     * Il client non deve mai vedere l'oggetto Book "grezzo".
     */
    private BookDTO toDTO(Book book) {
        // TODO: Implementare qui (riempire id, title, author, year)
        return null;
    }
}