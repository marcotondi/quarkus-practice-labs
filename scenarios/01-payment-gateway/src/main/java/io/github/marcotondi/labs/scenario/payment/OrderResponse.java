package io.github.marcotondi.labs.scenario.payment;

import java.math.BigDecimal;

public class OrderResponse {

    public Long id;

    public BigDecimal amount;

    public String currency;

    public OrderStatus status;

    public OrderResponse() {
    }

    public OrderResponse(Long id, BigDecimal amount, String currency, OrderStatus status) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}
