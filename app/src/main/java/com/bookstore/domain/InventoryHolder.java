package com.bookstore.domain;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "inventory_holders")
public abstract class InventoryHolder {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToMany(mappedBy = "inventoryHolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryItem> inventoryItems = new HashSet<>();

    @Column(nullable = false)
    private String address;

    public UUID getId() {
        return id;
    }

    public Set<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public String getAddress() {
        return address;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setInventoryItems(Set<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    protected void setAddress(String address) {
        this.address = Objects.requireNonNull(address, "Address cannot be null");
    }
}
