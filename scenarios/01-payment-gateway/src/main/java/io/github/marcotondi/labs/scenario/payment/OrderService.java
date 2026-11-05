package io.github.marcotondi.labs.scenario.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository repository;

    @Inject
    PaymentGateway gateway;

    public OrderResponse place(OrderRequest request) {
        throw new UnsupportedOperationException();
    }

    public OrderResponse get(Long id) {
        throw new UnsupportedOperationException();
    }
}
