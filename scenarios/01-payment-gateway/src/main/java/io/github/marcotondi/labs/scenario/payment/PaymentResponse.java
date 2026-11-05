package io.github.marcotondi.labs.scenario.payment;

public class PaymentResponse {

    public String paymentId;

    public String status;

    public PaymentResponse() {
    }

    public PaymentResponse(String paymentId, String status) {
        this.paymentId = paymentId;
        this.status = status;
    }
}
