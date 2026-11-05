package io.github.marcotondi.labs.scenario.subscription;

public class SubscriptionExpiredException extends RuntimeException {

    public SubscriptionExpiredException() {
        super("subscription expired");
    }
}
