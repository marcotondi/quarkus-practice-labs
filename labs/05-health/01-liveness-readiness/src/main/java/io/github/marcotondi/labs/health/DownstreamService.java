package io.github.marcotondi.labs.health;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Componente che monitoriamo con un health check: può essere
 * "su" o "giù". I test lo controllano per verificare il comportamento
 * degli endpoint di health.
 *
 * NON modificare.
 */
@ApplicationScoped
public class DownstreamService {

    private volatile boolean up = true;

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
}