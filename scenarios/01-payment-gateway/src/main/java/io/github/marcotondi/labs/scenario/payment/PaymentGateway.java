package io.github.marcotondi.labs.scenario.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PaymentGateway {

    @Inject
    @RestClient
    PaymentClient client;

    public PaymentResponse charge(PaymentRequest request) {
        return client.authorize(request);
    }
}
