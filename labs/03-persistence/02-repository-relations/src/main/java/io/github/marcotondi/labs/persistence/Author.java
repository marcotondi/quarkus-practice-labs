package io.github.marcotondi.labs.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entità Author con relazione bidirezionale @OneToMany verso Book.
 * NON modificare.
 */
@Entity
public class Author extends PanacheEntity {

    public String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Book> books = new ArrayList<>();

    /** Aggiunge un libro mantenendo coerente entrambi i lati della relazione. */
    public void addBook(String title) {
        Book book = new Book();
        book.title = title;
        book.author = this;
        books.add(book);
    }
}