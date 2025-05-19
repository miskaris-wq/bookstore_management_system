package test;

import com.bookstore.dao.api.BookRepository;
import com.bookstore.dao.api.PhysicStoreRepository;
import com.bookstore.dao.api.WarehouseRepository;
import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.PhysicStore;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.services.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private WarehouseRepository warehouseRepository;
    private PhysicStoreRepository storeRepository;
    private BookRepository bookRepository;
    private InventoryHolder warehouseInventory;
    private InventoryHolder storeInventory;

    private InventoryService inventoryService;
    private Book testBook;

    private Warehouse warehouse;
    private PhysicStore store;

    @BeforeEach
    void setUp() {
        warehouseRepository = mock(WarehouseRepository.class);
        storeRepository = mock(PhysicStoreRepository.class);
        bookRepository = mock(BookRepository.class);
        warehouseInventory = mock(InventoryHolder.class);
        storeInventory = mock(InventoryHolder.class);

        inventoryService = new InventoryService(
                warehouseRepository,
                storeRepository,
                bookRepository,
                warehouseInventory,
                storeInventory
        );

        testBook = new Book(UUID.randomUUID(), "Test Book", "Author", "genre", BigDecimal.valueOf(19.99), 2020);

        warehouse = new Warehouse("Warehouse Address");
        store = new PhysicStore("Store Name", "Store Address");

        // mock inventory behavior
        when(warehouseInventory.getItemQuantity(testBook)).thenReturn(10);
        when(storeInventory.getItemQuantity(testBook)).thenReturn(5);

        when(bookRepository.findById(testBook.getId())).thenReturn(List.of(testBook));
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));
        when(storeRepository.findAll()).thenReturn(List.of(store));
    }

    @Test
    void getTotalStock_shouldReturnCorrectSum() {
        // given
        warehouse.getInventory().put(testBook, 10);
        store.getInventory().put(testBook, 5);

        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));
        when(storeRepository.findAll()).thenReturn(List.of(store));

        int total = inventoryService.getTotalStock(testBook);

        assertEquals(15, total);
    }

    @Test
    void redistributeStock_shouldSucceedWhenStockIsSufficient() {
        // given
        doNothing().when(warehouseInventory).removeItem(testBook, 5);
        doNothing().when(storeInventory).addItem(eq(testBook), anyInt());

        // when
        assertDoesNotThrow(() -> inventoryService.redistributeStock(testBook, 5));

        // then
        verify(warehouseInventory).removeItem(testBook, 5);
        verify(storeInventory, atLeastOnce()).addItem(eq(testBook), anyInt());
    }

    @Test
    void redistributeStock_shouldThrowWhenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.redistributeStock(null, 5));
    }

    @Test
    void redistributeStock_shouldThrowWhenBookNotFound() {
        when(bookRepository.findById(testBook.getId())).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.redistributeStock(testBook, 5));
    }

    @Test
    void redistributeStock_shouldThrowWhenStockIsInsufficient() {
        when(warehouseInventory.getItemQuantity(testBook)).thenReturn(2);
        when(storeInventory.getItemQuantity(testBook)).thenReturn(2);

        assertThrows(InsufficientStockException.class,
                () -> inventoryService.redistributeStock(testBook, 10));
    }

    @Test
    void redistributeStock_shouldDistributeEvenlyAcrossStores() throws InsufficientStockException {
        PhysicStore store1 = new PhysicStore("Store1", "Addr1");
        PhysicStore store2 = new PhysicStore("Store2", "Addr2");

        when(storeRepository.findAll()).thenReturn(List.of(store1, store2));
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));

        when(warehouseInventory.getItemQuantity(testBook)).thenReturn(6);
        doNothing().when(warehouseInventory).removeItem(testBook, 6);
        doNothing().when(storeInventory).addItem(eq(testBook), anyInt());

        inventoryService.redistributeStock(testBook, 6);

        verify(storeInventory, times(2)).addItem(testBook, 3);
    }
}
