package io.github.marcotondi.labs.scenario.inventory;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_items")
public class StockItem extends PanacheEntity {

    public String sku;

    public int quantity;

    public StockItem() {
    }

    public StockItem(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }
}
