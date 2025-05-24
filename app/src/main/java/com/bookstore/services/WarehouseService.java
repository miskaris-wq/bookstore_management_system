package com.bookstore.services;

import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.LocationNotFoundException;
import com.bookstore.repository.JpaWarehouseRepository;
import com.bookstore.services.impl.AbstractInventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class WarehouseService extends AbstractInventoryService {

    private final JpaWarehouseRepository warehouseRepository;

    public WarehouseService(JpaWarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    protected void validateLocation(UUID warehouseId) throws LocationNotFoundException {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new LocationNotFoundException(warehouseId);
        }
    }

    @Override
    protected Optional<? extends InventoryHolder> findInventoryHolderById(UUID locationId) {
        return warehouseRepository.findById(locationId);
    }

    @Override
    protected void saveInventoryHolder(InventoryHolder holder) {
        if (holder instanceof Warehouse warehouse) {
            warehouseRepository.save(warehouse);
        } else {
            throw new IllegalArgumentException("Expected warehouse, got: " + holder.getClass());
        }
    }
}
