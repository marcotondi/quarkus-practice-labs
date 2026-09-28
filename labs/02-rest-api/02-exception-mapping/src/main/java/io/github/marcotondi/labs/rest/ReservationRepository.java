package io.github.marcotondi.labs.rest;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository in-memory, pre-popolato. NON modificare.
 */
@ApplicationScoped
public class ReservationRepository {

    private final Map<Long, Reservation> byId = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong(2);

    public ReservationRepository() {
        byId.put(1L, new Reservation(1L, "R-100", "Alice", 2));
    }

    public Reservation findById(Long id) {
        return byId.get(id);
    }

    public Reservation findByCode(String code) {
        return byId.values().stream()
                .filter(r -> r.code != null && r.code.equals(code))
                .findFirst()
                .orElse(null);
    }

    public Reservation save(Reservation reservation) {
        reservation.id = ids.getAndIncrement();
        byId.put(reservation.id, reservation);
        return reservation;
    }
}