package com.bookstore.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouses")
public class Warehouse extends InventoryHolder {

    public Warehouse() {}

    public Warehouse(String address) {
        setAddress(address);
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "ID=" + getId() +
                ", Address='" + getAddress() + '\'' +
                '}';
    }
}
