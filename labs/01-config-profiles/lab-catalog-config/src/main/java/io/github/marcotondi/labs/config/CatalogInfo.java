package io.github.marcotondi.labs.config;

/**
 * DTO di risposta: espone la configurazione risolta dal profilo attivo.
 * NON modificare.
 */
public class CatalogInfo {

    public String greeting;
    public int itemsPerPage;
    public String datasourceUrl;
    public boolean newUiEnabled;

    public CatalogInfo() {
    }

    public CatalogInfo(String greeting, int itemsPerPage, String datasourceUrl, boolean newUiEnabled) {
        this.greeting = greeting;
        this.itemsPerPage = itemsPerPage;
        this.datasourceUrl = datasourceUrl;
        this.newUiEnabled = newUiEnabled;
    }
}