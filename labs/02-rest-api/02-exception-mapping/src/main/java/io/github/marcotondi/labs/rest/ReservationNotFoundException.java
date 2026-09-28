package io.github.marcotondi.labs.rest;

/**
 * Lanciata quando la prenotazione richiesta non esiste. NON modificare.
 */
public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Prenotazione " + id + " non trovata");
    }
}