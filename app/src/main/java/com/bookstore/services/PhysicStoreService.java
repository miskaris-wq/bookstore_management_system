package com.bookstore.services;

import com.bookstore.dao.api.PhysicStoreRepository;
import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.PhysicStore;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.LocationNotFoundException;
import com.bookstore.exceptions.PhysicStoreNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PhysicStoreService extends AbstractInventoryService {

    private final PhysicStoreRepository storeRepository;

    public PhysicStoreService(PhysicStoreRepository storeRepository,
                              InventoryHolder storeInventory) {
        super(storeInventory);
        this.storeRepository = storeRepository;
    }

    @Override
    protected void validateLocation(UUID storeId) throws LocationNotFoundException {
        List<PhysicStore> store = storeRepository.findById(storeId);
        if (store == null || store.isEmpty()) {
            throw new LocationNotFoundException(storeId);
        }
    }

    protected InsufficientStockException createInsufficientStockException(Book book, int requestedQuantity) {
        int available = inventoryHolder.getItemQuantity(book);
        return new InsufficientStockException(book.getTitle(), available, requestedQuantity);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void addBook(UUID locationId, Book book, int quantity) {
        if (book == null) throw new NullPointerException("Book must not be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        super.addBook(locationId, book, quantity);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void removeBook(UUID locationId, Book book, int quantity) throws InsufficientStockException {
        super.removeBook(locationId, book, quantity);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public Map<Book, Integer> getInventory(UUID locationId) {
        return super.getInventory(locationId);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public List<Book> getLowStockBooks(UUID locationId, int threshold) {
        return super.getLowStockBooks(locationId, threshold);
    }
}

