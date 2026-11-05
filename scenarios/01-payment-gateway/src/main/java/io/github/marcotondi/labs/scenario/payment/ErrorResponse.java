package io.github.marcotondi.labs.scenario.payment;

public class ErrorResponse {

    public String error;

    public Long orderId;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, Long orderId) {
        this.error = error;
        this.orderId = orderId;
    }
}
