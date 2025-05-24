package com.bookstore.repository;

import com.bookstore.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaWarehouseRepository extends JpaRepository<Warehouse, UUID> {
    List<Warehouse> findByAddressContainingIgnoreCase(String address);
}
