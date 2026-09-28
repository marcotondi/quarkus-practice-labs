package io.github.marcotondi.labs.catalog;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Entità Hibernate ORM con Panache Active Record.
 * Estendendo PanacheEntity erediti l'id e i metodi statici di query
 * (listAll, findById, count, list, ...).
 *
 * NON modificare: l'entità è già completa.
 */
@Entity
public class Product extends PanacheEntity {

    public String name;
    public BigDecimal price;
    public int stock;

    public Product() {
    }

    public Product(String name, BigDecimal price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}