package io.github.marcotondi.labs.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: implementa le query con il pattern PanacheRepository.
 *
 * Le query devono usare "left join fetch" per caricare i libri
 * insieme all'autore (evita il problema del lazy loading fuori
 * transazione).
 */
@ApplicationScoped
public class AuthorRepository implements PanacheRepository<Author> {

    // TODO: Implementare qui
    // Cerca un autore per nome esatto, con i libri già caricati.
    public Optional<Author> findByName(String name) {
        return Optional.empty();
    }

    // TODO: Implementare qui
    // Cerca un autore per id, con i libri già caricati.
    public Optional<Author> findByIdWithBooks(Long id) {
        return Optional.empty();
    }

    // TODO: Implementare qui
    // Elenca tutti gli autori con i libri già caricati.
    public List<Author> listWithBooks() {
        return List.of();
    }
}