package com.bookstore.domain;

import java.util.UUID;
import java.util.Objects;

public class Warehouse extends InventoryHolder {
    private final UUID id;
    private final String address;

    public Warehouse(String address) {
        this.id = UUID.randomUUID();
        this.address = Objects.requireNonNull(address, "Address cannot be null");
    }

    // Уникальные методы для Warehouse
    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Warehouse {" +
                "ID='" + id + '\'' +
                ", Address='" + address + '\'' +
                ", TotalItems=" + getInventory().values().stream().mapToInt(Integer::intValue).sum() +
                ", UniqueItems=" + getInventory().size() +
                '}';
    }
}
