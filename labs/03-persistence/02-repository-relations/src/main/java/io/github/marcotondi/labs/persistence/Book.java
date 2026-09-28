package io.github.marcotondi.labs.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

/**
 * Entità Book, lato "many" della relazione. NON modificare.
 */
@Entity
public class Book extends PanacheEntity {

    public String title;

    @ManyToOne(fetch = FetchType.LAZY)
    public Author author;
}