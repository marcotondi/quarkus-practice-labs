package io.github.marcotondi.labs.faulttolerance;

/**
 * Errore del servizio esterno (downstream). NON modificare.
 */
public class DownstreamException extends RuntimeException {

    public DownstreamException(String message) {
        super(message);
    }
}