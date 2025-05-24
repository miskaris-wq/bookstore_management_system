package com.bookstore.services;

import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.PhysicStore;
import com.bookstore.exceptions.LocationNotFoundException;
import com.bookstore.repository.JpaPhysicStoreRepository;
import com.bookstore.services.impl.AbstractInventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PhysicStoreService extends AbstractInventoryService {

    private final JpaPhysicStoreRepository storeRepository;

    public PhysicStoreService(JpaPhysicStoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    protected void validateLocation(UUID storeId) throws LocationNotFoundException {
        if (!storeRepository.existsById(storeId)) {
            throw new LocationNotFoundException(storeId);
        }
    }

    @Override
    protected Optional<? extends InventoryHolder> findInventoryHolderById(UUID locationId) {
        return storeRepository.findById(locationId);
    }

    @Override
    protected void saveInventoryHolder(InventoryHolder holder) {
        if (holder instanceof PhysicStore store) {
            storeRepository.save(store);
        } else {
            throw new IllegalArgumentException("Expected PhysicStore, got " + holder.getClass());
        }
    }

}
