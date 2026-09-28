package io.github.marcotondi.labs.openapi;

/**
 * DTO esposto dall'API.
 */
public class Widget {

    public Long id;

    // TODO: Implementare qui
    // Documenta questo campo con @Schema(description = "...", example = "...")
    // usando org.eclipse.microprofile.openapi.annotations.media.Schema.
    public String name;

    public Widget() {
    }

    public Widget(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}