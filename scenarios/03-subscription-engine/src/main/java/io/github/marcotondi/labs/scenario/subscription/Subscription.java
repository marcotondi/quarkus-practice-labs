package io.github.marcotondi.labs.scenario.subscription;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription extends PanacheEntity {

    public String plan;

    public BigDecimal monthlyPrice;

    @Enumerated(EnumType.STRING)
    public SubscriptionStatus status;

    public LocalDate expiresAt;

    public Subscription() {
    }

    public Subscription(String plan, BigDecimal monthlyPrice, SubscriptionStatus status, LocalDate expiresAt) {
        this.plan = plan;
        this.monthlyPrice = monthlyPrice;
        this.status = status;
        this.expiresAt = expiresAt;
    }
}
