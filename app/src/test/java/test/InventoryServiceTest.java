package test;

import com.bookstore.domain.Book;
import com.bookstore.domain.PhysicStore;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.repository.JpaBookRepository;
import com.bookstore.repository.JpaPhysicStoreRepository;
import com.bookstore.repository.JpaWarehouseRepository;
import com.bookstore.services.InventoryService;
import com.bookstore.services.PhysicStoreService;
import com.bookstore.services.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private JpaWarehouseRepository warehouseRepository;
    private JpaPhysicStoreRepository storeRepository;
    private JpaBookRepository bookRepository;
    private WarehouseService warehouseService;
    private PhysicStoreService storeService;

    private InventoryService inventoryService;
    private Book testBook;

    private Warehouse warehouse;
    private PhysicStore store1;
    private PhysicStore store2;

    @BeforeEach
    void setUp() {
        warehouseRepository = mock(JpaWarehouseRepository.class);
        storeRepository = mock(JpaPhysicStoreRepository.class);
        bookRepository = mock(JpaBookRepository.class);
        warehouseService = mock(WarehouseService.class);
        storeService = mock(PhysicStoreService.class);

        inventoryService = new InventoryService(
                warehouseRepository,
                storeRepository,
                bookRepository,
                warehouseService,
                storeService
        );

        testBook = new Book(UUID.randomUUID(), "Test Book", "Author", "genre", BigDecimal.valueOf(19.99), 2020);

        warehouse = new Warehouse("Warehouse Address");
        store1 = new PhysicStore("Store1", "Addr1");
        store2 = new PhysicStore("Store2", "Addr2");
    }

    @Test
    void getTotalStock_shouldReturnCorrectSum() {
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));
        when(storeRepository.findAll()).thenReturn(List.of(store1));

        // Используем внутренний метод inventoryService
        warehouse.getInventoryItems().add(new com.bookstore.domain.InventoryItem(testBook, warehouse, 10));
        store1.getInventoryItems().add(new com.bookstore.domain.InventoryItem(testBook, store1, 5));

        int total = inventoryService.getTotalStock(testBook);

        assertEquals(15, total);
    }

    @Test
    void redistributeStock_shouldThrowWhenBookIsNull() {
        assertThrows(NullPointerException.class,
                () -> inventoryService.redistributeStock(null, 5));
    }

    @Test
    void redistributeStock_shouldThrowWhenBookNotFound() {
        when(bookRepository.existsById(testBook.getId())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.redistributeStock(testBook, 5));

        assertEquals("Book does not exist", ex.getMessage());
    }

    @Test
    void redistributeStock_shouldSucceedWhenStockIsSufficient() throws InsufficientStockException {
        when(bookRepository.existsById(testBook.getId())).thenReturn(true);
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));
        when(storeRepository.findAll()).thenReturn(List.of(store1));

        warehouse.getInventoryItems().add(new com.bookstore.domain.InventoryItem(testBook, warehouse, 10));

        doNothing().when(warehouseService).removeBook(warehouse.getId(), testBook, 5);
        doNothing().when(storeService).addBook(store1.getId(), testBook, 5);

        inventoryService.redistributeStock(testBook, 5);

        verify(warehouseService).removeBook(warehouse.getId(), testBook, 5);
        verify(storeService).addBook(store1.getId(), testBook, 5);
    }
}
