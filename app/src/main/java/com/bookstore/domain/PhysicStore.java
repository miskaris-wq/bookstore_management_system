package com.bookstore.domain;

import java.util.UUID;
import java.util.Objects;

public class PhysicStore extends InventoryHolder {
    private final UUID id;
    private final String name;
    private final String address;

    public PhysicStore(String name, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Store name cannot be empty");
        }
        this.id = UUID.randomUUID();
        this.name = name.trim();
        this.address = Objects.requireNonNull(address, "Address cannot be null");
    }

    // Уникальные методы для PhysicStore
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Store {" +
                "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Address='" + address + '\'' +
                ", TotalItems=" + getInventory().values().stream().mapToInt(Integer::intValue).sum() +
                ", UniqueItems=" + getInventory().size() +
                '}';
    }
}
