package io.github.marcotondi.labs.scenario.payment;

public class PaymentUnavailableException extends RuntimeException {

    private final Long orderId;

    public PaymentUnavailableException(Long orderId) {
        super("payment gateway unavailable");
        this.orderId = orderId;
    }

    public Long orderId() {
        return orderId;
    }
}
