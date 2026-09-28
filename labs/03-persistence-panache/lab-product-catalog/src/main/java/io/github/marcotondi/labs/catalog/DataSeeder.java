package io.github.marcotondi.labs.catalog;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

/**
 * Popola il database all'avvio se è vuoto.
 * NON modificare: è codice di esempio su come usare Panache
 * (persist all'interno di una transazione).
 */
@ApplicationScoped
public class DataSeeder {

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (Product.count() == 0) {
            new Product("Mechanical Keyboard", new BigDecimal("89.90"), 12).persist();
            new Product("Wireless Mouse", new BigDecimal("24.50"), 0).persist();
            new Product("USB-C Hub", new BigDecimal("39.00"), 7).persist();
        }
    }
}