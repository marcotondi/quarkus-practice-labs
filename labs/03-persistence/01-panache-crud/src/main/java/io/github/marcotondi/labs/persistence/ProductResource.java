package io.github.marcotondi.labs.persistence;

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
 * REST endpoint: /api/products
 *
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: implementali usando Panache (Active Record).
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @GET
    public List<Product> list() {
        // TODO: Implementare qui -> Product.listAll()
        return null;
    }

    @GET
    @Path("/search")
    public List<Product> search(@QueryParam("name") String name) {
        // TODO: Implementare qui
        // Query Panache case-insensitive sul nome, es:
        // Product.list("lower(name) like ?1", "%" + name.toLowerCase() + "%")
        return null;
    }

    @GET
    @Path("/{id}")
    public Product get(@PathParam("id") Long id) {
        // TODO: Implementare qui -> Product.findById(id)
        // Se il prodotto non esiste lancia new jakarta.ws.rs.NotFoundException()
        return null;
    }

    @POST
    public Response create(Product product) {
        // TODO: Implementare qui
        // 1. Annota il metodo con @Transactional
        // 2. product.persist() (l'id viene assegnato dal DB)
        // 3. Rispondi 201 Created con header Location /api/products/{id}
        //    e body = prodotto creato
        return null;
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Product product) {
        // TODO: Implementare qui
        // 1. Annota il metodo con @Transactional
        // 2. Trova il prodotto; se assente -> 404
        // 3. Aggiorna name/price/stock (l'entità è managed: basta
        //    modificarla, il flush avviene a fine transazione)
        // 4. Rispondi 200 con il prodotto aggiornato
        return null;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        // TODO: Implementare qui
        // 1. Annota il metodo con @Transactional
        // 2. Trova il prodotto; se assente -> 404
        // 3. product.delete() e rispondi 204 No Content
        return null;
    }
}