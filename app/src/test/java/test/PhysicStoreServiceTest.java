package test;

import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryItem;
import com.bookstore.domain.PhysicStore;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.LocationNotFoundException;
import com.bookstore.repository.JpaPhysicStoreRepository;
import com.bookstore.services.PhysicStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhysicStoreServiceTest {

    private JpaPhysicStoreRepository storeRepository;
    private PhysicStoreService storeService;

    private UUID storeId;
    private PhysicStore store;
    private Book book;

    @BeforeEach
    void setUp() {
        storeRepository = mock(JpaPhysicStoreRepository.class);
        storeService = new PhysicStoreService(storeRepository);

        storeId = UUID.randomUUID();
        store = new PhysicStore();
        store.setId(storeId);
        store.setInventoryItems(new HashSet<>());

        book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Clean Code");
    }

    @Test
    void addBook_newBook_addsToInventory() {
        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        storeService.addBook(storeId, book, 5);

        assertEquals(1, store.getInventoryItems().size());
        InventoryItem item = store.getInventoryItems().iterator().next();
        assertEquals(book, item.getBook());
        assertEquals(5, item.getQuantity());
        verify(storeRepository).save(store);
    }

    @Test
    void addBook_existingBook_incrementsQuantity() {
        InventoryItem existing = new InventoryItem(book, store, 3);
        store.getInventoryItems().add(existing);

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        storeService.addBook(storeId, book, 2);

        assertEquals(1, store.getInventoryItems().size());
        InventoryItem item = store.getInventoryItems().iterator().next();
        assertEquals(5, item.getQuantity());
        verify(storeRepository).save(store);
    }

    @Test
    void removeBook_sufficientStock_decreasesQuantity() throws InsufficientStockException {
        InventoryItem item = new InventoryItem(book, store, 10);
        store.getInventoryItems().add(item);

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        storeService.removeBook(storeId, book, 4);

        assertEquals(6, item.getQuantity());
        verify(storeRepository).save(store);
    }

    @Test
    void removeBook_insufficientStock_throwsException() {
        InventoryItem item = new InventoryItem(book, store, 2);
        store.getInventoryItems().add(item);

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        assertThrows(InsufficientStockException.class, () -> storeService.removeBook(storeId, book, 3));
    }

    @Test
    void removeBook_removesItemWhenZero() throws InsufficientStockException {
        InventoryItem item = new InventoryItem(book, store, 2);
        store.getInventoryItems().add(item);

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        storeService.removeBook(storeId, book, 2);

        assertTrue(store.getInventoryItems().isEmpty());
        verify(storeRepository).save(store);
    }

    @Test
    void getInventory_returnsCorrectMap() {
        store.getInventoryItems().add(new InventoryItem(book, store, 8));

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        Map<Book, Integer> inventory = storeService.getInventory(storeId);

        assertEquals(1, inventory.size());
        assertEquals(8, inventory.get(book));
    }

    @Test
    void getLowStockBooks_returnsBooksBelowThreshold() {
        Book anotherBook = new Book();
        anotherBook.setId(UUID.randomUUID());
        anotherBook.setTitle("Refactoring");

        store.getInventoryItems().add(new InventoryItem(book, store, 3));
        store.getInventoryItems().add(new InventoryItem(anotherBook, store, 10));

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        List<Book> result = storeService.getLowStockBooks(storeId, 5);

        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    void validateLocation_notExists_throwsLocationNotFoundException() {
        when(storeRepository.existsById(storeId)).thenReturn(false);

        assertThrows(LocationNotFoundException.class, () -> storeService.getInventory(storeId));
    }
}
