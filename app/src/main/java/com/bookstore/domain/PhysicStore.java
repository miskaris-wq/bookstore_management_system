package com.bookstore.domain;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "physic_stores")
public class PhysicStore extends InventoryHolder {

    @Column(nullable = false)
    private String name;

    public PhysicStore() {}

    public PhysicStore(String name, String address) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        setAddress(address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhysicStore that = (PhysicStore) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public String toString() {
        return "PhysicStore{" +
                "ID=" + getId() +
                ", Name='" + name + '\'' +
                ", Address='" + getAddress() + '\'' +
                '}';
    }
}
