package com.redhat.ex378.rest;

/**
 * Eccezione da lanciare quando un libro richiesto non esiste.
 * Il mapping a una risposta HTTP 404 è già configurato
 * (vedi BookNotFoundExceptionMapper): usa e getta.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Libro con id " + id + " non trovato");
    }
}