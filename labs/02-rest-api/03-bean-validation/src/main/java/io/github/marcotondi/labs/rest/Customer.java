package io.github.marcotondi.labs.rest;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: aggiungi i vincoli di validazione Jakarta Validation.
 *
 * Regole:
 *  - name: non blank, lunghezza 2..50
 *  - email: non blank, email valida
 *  - age: 18..120
 *  - loyaltyCode: formato "AAA-999" (3 lettere maiuscole, trattino, 3 cifre)
 */
public class Customer {

    public Long id;

    // TODO: Implementare qui
    public String name;

    // TODO: Implementare qui
    public String email;

    // TODO: Implementare qui
    public int age;

    // TODO: Implementare qui
    public String loyaltyCode;
}