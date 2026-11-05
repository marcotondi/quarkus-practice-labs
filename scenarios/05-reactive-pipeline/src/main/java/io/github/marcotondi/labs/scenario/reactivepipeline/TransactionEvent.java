package io.github.marcotondi.labs.scenario.reactivepipeline;

import java.math.BigDecimal;

public class TransactionEvent {

    public String transactionId;

    public BigDecimal amount;

    public String currency;

    public TransactionEvent() {
    }

    public TransactionEvent(String transactionId, BigDecimal amount, String currency) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
    }
}
