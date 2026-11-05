package io.github.marcotondi.labs.scenario.reactivepipeline;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class Transaction extends PanacheEntity {

    @Column(unique = true, nullable = false)
    public String transactionId;

    public BigDecimal amount;

    public String currency;

    @Enumerated(EnumType.STRING)
    public TransactionStatus status;

    public Transaction() {
    }

    public Transaction(String transactionId, BigDecimal amount, String currency, TransactionStatus status) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}
