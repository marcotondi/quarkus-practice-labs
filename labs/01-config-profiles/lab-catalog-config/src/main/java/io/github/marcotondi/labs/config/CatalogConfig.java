package io.github.marcotondi.labs.config;

import io.smallrye.config.ConfigMapping;

/**
 * Configurazione tipizzata dell'applicazione (prefix "catalog").
 *
 * Le chiavi nel file di configurazione sono in kebab-case e vengono
 * mappate automaticamente sui metodi in camelCase:
 *   catalog.items-per-page  ->  itemsPerPage()
 *   catalog.new-ui-enabled  ->  newUiEnabled()
 *
 * NON modificare questo file: la mappatura è già corretta. Il task è
 * fornire i valori giusti per ciascun profilo in application.properties.
 */
@ConfigMapping(prefix = "catalog")
public interface CatalogConfig {

    String greeting();

    int itemsPerPage();

    String datasourceUrl();

    boolean newUiEnabled();
}