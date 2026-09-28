package io.github.marcotondi.labs.rest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Modello della prenotazione, con le regole di validazione già definite.
 * NON modificare.
 */
public class Reservation {

    public Long id;

    @NotBlank(message = "code è obbligatorio")
    public String code;

    @NotBlank(message = "customer è obbligatorio")
    public String customer;

    @Min(value = 1, message = "seats: minimo 1")
    public int seats;

    public Reservation() {
    }

    public Reservation(Long id, String code, String customer, int seats) {
        this.id = id;
        this.code = code;
        this.customer = customer;
        this.seats = seats;
    }
}