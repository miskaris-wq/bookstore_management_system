package com.bookstore.dto;

import java.util.Map;
import java.util.UUID;

public class WarehouseDTO {
    private UUID id;
    private String address;
    private Map<BookDTO, Integer> inventory;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<BookDTO, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<BookDTO, Integer> inventory) {
        this.inventory = inventory;
    }
}
