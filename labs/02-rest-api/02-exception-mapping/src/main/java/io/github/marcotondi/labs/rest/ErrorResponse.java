package io.github.marcotondi.labs.rest;

/**
 * Corpo di errore uniforme dell'API. NON modificare.
 */
public class ErrorResponse {

    public String code;
    public String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}