package io.github.marcotondi.labs.restclient;

/**
 * Eccezione locale che rappresenta un "widget non trovato" sul servizio
 * remoto. NON modificare.
 */
public class WidgetNotFoundException extends RuntimeException {

    public WidgetNotFoundException(Long id) {
        super("Widget " + id + " non trovato sul servizio remoto");
    }
}