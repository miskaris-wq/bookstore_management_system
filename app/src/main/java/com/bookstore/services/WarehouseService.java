package com.bookstore.services;

import com.bookstore.dao.api.WarehouseRepository;
import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.LocationNotFoundException;
import com.bookstore.exceptions.WarehouseNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WarehouseService extends AbstractInventoryService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository,
                            InventoryHolder warehouseInventory) {
        super(warehouseInventory);
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    protected void validateLocation(UUID warehouseId) throws LocationNotFoundException {
        List<Warehouse> warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null || warehouse.isEmpty()) {
            throw new LocationNotFoundException(warehouseId);
        }
    }

    protected InsufficientStockException createInsufficientStockException(Book book, int requestedQuantity) {
        int available = inventoryHolder.getItemQuantity(book);
        return new InsufficientStockException(book.getTitle(), available, requestedQuantity);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER')")
    public void addBook(UUID locationId, Book book, int quantity) {
        if (book == null) throw new NullPointerException("Book must not be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        super.addBook(locationId, book, quantity);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER')")
    public void removeBook(UUID locationId, Book book, int quantity) throws InsufficientStockException {
        super.removeBook(locationId, book, quantity);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER', 'MANAGER')")
    public Map<Book, Integer> getInventory(UUID locationId) {
        return super.getInventory(locationId);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER', 'MANAGER')")
    public List<Book> getLowStockBooks(UUID locationId, int threshold) {
        if (threshold <= 0) throw new IllegalArgumentException("threshold must be positive");
        return super.getLowStockBooks(locationId, threshold);
    }
}

