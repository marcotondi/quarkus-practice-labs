package io.github.marcotondi.labs.scenario.payment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

@Entity
public class Order extends PanacheEntity {

    public BigDecimal amount;

    public String currency;

    @Enumerated(EnumType.STRING)
    public OrderStatus status;

    public Order() {
    }

    public Order(BigDecimal amount, String currency, OrderStatus status) {
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}
