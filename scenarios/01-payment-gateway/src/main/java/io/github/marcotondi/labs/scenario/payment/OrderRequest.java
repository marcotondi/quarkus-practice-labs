package io.github.marcotondi.labs.scenario.payment;

import java.math.BigDecimal;

public class OrderRequest {

    public BigDecimal amount;

    public String currency;

    public OrderRequest() {
    }

    public OrderRequest(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
