package test;

import com.bookstore.dao.api.PhysicStoreRepository;
import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.PhysicStore;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.services.PhysicStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhysicStoreServiceTest {

    private PhysicStoreRepository storeRepository;
    private InventoryHolder storeInventory;
    private PhysicStoreService storeService;

    private Book testBook;
    private UUID storeId;
    private PhysicStore store;

    @BeforeEach
    void setUp() {
        storeRepository = mock(PhysicStoreRepository.class);
        storeInventory = mock(InventoryHolder.class);
        storeService = new PhysicStoreService(storeRepository, storeInventory);

        testBook = new Book("Test Book", "Author", "Genre", new BigDecimal("45.99"), 2022);
        store = new PhysicStore("Test Store", "Test Address");
        storeId = store.getId();
    }

    @Test
    void addBook_shouldAddBookIfStoreExists() throws Exception {
        when(storeRepository.findById(storeId)).thenReturn(List.of(store));

        storeService.addBook(storeId, testBook, 5);

        verify(storeInventory).addItem(testBook, 5);
    }

    @Test
    void addBook_shouldThrowExceptionIfBookIsNull() {
        assertThrows(NullPointerException.class, () ->
                storeService.addBook(storeId, null, 5));
    }

    @Test
    void addBook_shouldThrowExceptionIfQuantityIsNonPositive() {
        assertThrows(IllegalArgumentException.class, () ->
                storeService.addBook(storeId, testBook, 0));
    }

    @Test
    void addBook_shouldThrowIfStoreNotFound() {
        when(storeRepository.findById(storeId)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () ->
                storeService.addBook(storeId, testBook, 1));
    }

    @Test
    void removeBook_shouldRemoveBookIfEnoughStock() throws Exception {
        when(storeRepository.findById(storeId)).thenReturn(List.of(store));
        doNothing().when(storeInventory).removeItem(testBook, 3);

        storeService.removeBook(storeId, testBook, 3);

        verify(storeInventory).removeItem(testBook, 3);
    }

    @Test
    void removeBook_shouldThrowInsufficientStockException() throws Exception {
        when(storeRepository.findById(storeId)).thenReturn(List.of(store));
        when(storeInventory.getItemQuantity(testBook)).thenReturn(2);
        doThrow(new IllegalStateException()).when(storeInventory).removeItem(testBook, 3);

        InsufficientStockException ex = assertThrows(InsufficientStockException.class, () ->
                storeService.removeBook(storeId, testBook, 3));

        assertTrue(ex.getMessage().contains(testBook.getTitle()));
    }

    @Test
    void getInventory_shouldReturnInventoryIfStoreExists() throws Exception {
        Map<Book, Integer> inventoryMap = Map.of(testBook, 10);
        when(storeRepository.findById(storeId)).thenReturn(List.of(store));
        when(storeInventory.getInventory()).thenReturn(inventoryMap);

        Map<Book, Integer> result = storeService.getInventory(storeId);

        assertEquals(10, result.get(testBook));
    }

    @Test
    void getLowStockBooks_shouldReturnFilteredList() throws Exception {
        when(storeRepository.findById(storeId)).thenReturn(List.of(store));
        when(storeInventory.getInventory()).thenReturn(Map.of(testBook, 2));

        List<Book> lowStockBooks = storeService.getLowStockBooks(storeId, 5);

        assertEquals(1, lowStockBooks.size());
        assertEquals(testBook, lowStockBooks.getFirst());
    }

    @Test
    void getLowStockBooks_shouldReturnEmptyListIfNoBooksBelowThreshold() throws Exception {
        when(storeRepository.findById(storeId)).thenReturn(List.of(store));
        when(storeInventory.getInventory()).thenReturn(Map.of(testBook, 10));

        List<Book> lowStockBooks = storeService.getLowStockBooks(storeId, 5);

        assertTrue(lowStockBooks.isEmpty());
    }
}
