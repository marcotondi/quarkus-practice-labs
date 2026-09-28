package io.github.marcotondi.labs.rest;

/**
 * Lanciata quando esiste già una prenotazione con lo stesso code.
 * NON modificare.
 */
public class ReservationConflictException extends RuntimeException {

    public ReservationConflictException(String code) {
        super("Esiste già una prenotazione con code " + code);
    }
}