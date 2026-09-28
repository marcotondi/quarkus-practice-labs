package io.github.marcotondi.labs.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Risposta autore (con i titoli dei libri). NON modificare.
 */
public class AuthorResponse {

    public Long id;
    public String name;
    public List<String> bookTitles = new ArrayList<>();

    public AuthorResponse() {
    }

    public AuthorResponse(Long id, String name, List<String> bookTitles) {
        this.id = id;
        this.name = name;
        this.bookTitles = bookTitles;
    }
}