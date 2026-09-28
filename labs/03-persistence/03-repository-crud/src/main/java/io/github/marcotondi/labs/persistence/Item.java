package io.github.marcotondi.labs.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;

/**
 * Entità JPA "pura": NON estende PanacheEntity, quindi non ha metodi
 * Active Record. Per accedervi si usa il pattern Repository.
 * NON modificare.
 */
@Entity
public class Item {

    @Id
    @GeneratedValue
    public Long id;

    public String name;
    public BigDecimal price;
    public int quantity;

    public Item() {
    }

    public Item(String name, BigDecimal price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}