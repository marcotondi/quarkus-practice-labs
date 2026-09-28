package io.github.marcotondi.labs.restclient;

/**
 * DTO del widget restituito dal servizio remoto. NON modificare.
 */
public class Widget {

    public Long id;
    public String name;

    public Widget() {
    }

    public Widget(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}