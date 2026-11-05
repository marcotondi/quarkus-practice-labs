package io.github.marcotondi.labs.scenario.subscription;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriptionResponse {

    public Long id;

    public String plan;

    public SubscriptionStatus status;

    public BigDecimal monthlyPrice;

    public LocalDate expiresAt;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(Long id, String plan, SubscriptionStatus status,
                                BigDecimal monthlyPrice, LocalDate expiresAt) {
        this.id = id;
        this.plan = plan;
        this.status = status;
        this.monthlyPrice = monthlyPrice;
        this.expiresAt = expiresAt;
    }
}
