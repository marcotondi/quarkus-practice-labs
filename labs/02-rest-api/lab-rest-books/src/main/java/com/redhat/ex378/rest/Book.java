package com.redhat.ex378.rest;

/**
 * Entità del dominio. In questo laboratorio vive in memoria (nessuna
 * persistenza): in un task reale EX378 sarebbe un'entità Hibernate ORM.
 * NON modificare questo file: è la "logica di business" già funzionante.
 */
public class Book {

    private Long id;
    private String title;
    private String author;
    private int year;

    public Book() {
    }

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}