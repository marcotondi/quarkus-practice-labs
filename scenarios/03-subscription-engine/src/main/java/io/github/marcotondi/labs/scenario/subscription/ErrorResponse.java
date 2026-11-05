package io.github.marcotondi.labs.scenario.subscription;

public class ErrorResponse {

    public String error;

    public int code;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, int code) {
        this.error = error;
        this.code = code;
    }
}
