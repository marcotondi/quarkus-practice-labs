package com.redhat.ex378.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO esposto dall'API: il client non deve mai vedere l'entità interna.
 * Le annotazioni di validazione sono COMPLETE: il tuo compito è solo
 * attivarle sul parametro del metodo REST con @Valid.
 */
public class BookDTO {

    /** Impostato dal server (read-only per il client). */
    public Long id;

    @NotBlank(message = "title è obbligatorio")
    @Size(max = 120, message = "title: massimo 120 caratteri")
    public String title;

    @NotBlank(message = "author è obbligatorio")
    @Size(max = 80, message = "author: massimo 80 caratteri")
    public String author;

    @NotNull(message = "year è obbligatorio")
    @Min(value = 0, message = "year: minimo 0")
    @Max(value = 2024, message = "year: massimo 2024")
    public Integer year;
}