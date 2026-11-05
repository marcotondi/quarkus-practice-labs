package io.github.marcotondi.labs.scenario.payment;

import java.math.BigDecimal;

public class PaymentRequest {

    public BigDecimal amount;

    public String currency;

    public PaymentRequest() {
    }

    public PaymentRequest(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
