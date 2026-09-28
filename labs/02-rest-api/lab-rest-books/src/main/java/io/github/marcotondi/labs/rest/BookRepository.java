package io.github.marcotondi.labs.rest;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository in-memory, pre-popolato con due libri.
 * Simula il livello di persistenza: NON modificarlo.
 */
@ApplicationScoped
public class BookRepository {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public BookRepository() {
        seed("1984", "George Orwell", 1949);
        seed("Brave New World", "Aldous Huxley", 1932);
    }

    private void seed(String title, String author, int year) {
        save(new Book(title, author, year));
    }

    public List<Book> findAll() {
        return List.copyOf(books.values());
    }

    public Book findById(Long id) {
        return books.get(id);
    }

    public Book save(Book book) {
        book.setId(idGenerator.getAndIncrement());
        books.put(book.getId(), book);
        return book;
    }
}