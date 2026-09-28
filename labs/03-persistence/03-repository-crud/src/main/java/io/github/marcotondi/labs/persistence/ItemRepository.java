package io.github.marcotondi.labs.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: implementa le query custom con PanacheRepository.
 *
 * PanacheRepository mette già a disposizione: listAll(), findByIdOptional(id),
 * persist(entity), deleteById(id), count(), find(...), list(...).
 */
@ApplicationScoped
public class ItemRepository implements PanacheRepository<Item> {

    // TODO: Implementare qui
    // Cerca per nome esatto: list("name", name)
    public List<Item> findByName(String name) {
        return List.of();
    }

    // TODO: Implementare qui
    // Elementi con disponibilità > 0: list("quantity > 0")
    public List<Item> inStock() {
        return List.of();
    }
}