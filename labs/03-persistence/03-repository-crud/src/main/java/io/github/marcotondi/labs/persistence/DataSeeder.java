package io.github.marcotondi.labs.persistence;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

/**
 * Popola il database all'avvio tramite il repository.
 * NON modificare.
 */
@ApplicationScoped
public class DataSeeder {

    @Inject
    ItemRepository repository;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (repository.count() == 0) {
            repository.persist(new Item("Mechanical Keyboard", new BigDecimal("89.90"), 12));
            repository.persist(new Item("Wireless Mouse", new BigDecimal("24.50"), 0));
            repository.persist(new Item("USB-C Hub", new BigDecimal("39.00"), 7));
        }
    }
}