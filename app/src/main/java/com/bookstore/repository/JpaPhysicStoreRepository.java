package com.bookstore.repository;

import com.bookstore.domain.PhysicStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaPhysicStoreRepository extends JpaRepository<PhysicStore, UUID> {
    List<PhysicStore> findByNameContainingIgnoreCase(String name);
    List<PhysicStore> findByAddressContainingIgnoreCase(String address);
}
