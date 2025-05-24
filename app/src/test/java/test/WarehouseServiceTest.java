package test;

import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryItem;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.LocationNotFoundException;
import com.bookstore.repository.JpaWarehouseRepository;
import com.bookstore.services.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WarehouseServiceTest {

    private JpaWarehouseRepository warehouseRepository;
    private WarehouseService warehouseService;

    private UUID warehouseId;
    private Warehouse warehouse;
    private Book book;

    @BeforeEach
    void setUp() {
        warehouseRepository = mock(JpaWarehouseRepository.class);
        warehouseService = new WarehouseService(warehouseRepository);

        warehouseId = UUID.randomUUID();
        warehouse = new Warehouse();
        warehouse.setId(warehouseId);
        warehouse.setInventoryItems(new HashSet<>());

        book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Effective Java");
    }

    @Test
    void addBook_newBook_addsToInventory() {
        when(warehouseRepository.existsById(warehouseId)).thenReturn(true);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

        warehouseService.addBook(warehouseId, book, 10);

        assertEquals(1, warehouse.getInventoryItems().size());
        InventoryItem item = warehouse.getInventoryItems().iterator().next();
        assertEquals(book, item.getBook());
        assertEquals(10, item.getQuantity());
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void addBook_existingBook_incrementsQuantity() {
        InventoryItem existing = new InventoryItem(book, warehouse, 5);
        warehouse.getInventoryItems().add(existing);

        when(warehouseRepository.existsById(warehouseId)).thenReturn(true);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

        warehouseService.addBook(warehouseId, book, 3);

        assertEquals(1, warehouse.getInventoryItems().size());
        InventoryItem item = warehouse.getInventoryItems().iterator().next();
        assertEquals(8, item.getQuantity());
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void removeBook_sufficientStock_decreasesQuantity() throws InsufficientStockException {
        InventoryItem item = new InventoryItem(book, warehouse, 7);
        warehouse.getInventoryItems().add(item);

        when(warehouseRepository.existsById(warehouseId)).thenReturn(true);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

        warehouseService.removeBook(warehouseId, book, 4);

        assertEquals(3, item.getQuantity());
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void removeBook_insufficientStock_throwsException() {
        InventoryItem item = new InventoryItem(book, warehouse, 2);
        warehouse.getInventoryItems().add(item);

        when(warehouseRepository.existsById(warehouseId)).thenReturn(true);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

        assertThrows(InsufficientStockException.class, () -> warehouseService.removeBook(warehouseId, book, 5));
    }

    @Test
    void removeBook_removesItemWhenZero() throws InsufficientStockException {
        InventoryItem item = new InventoryItem(book, warehouse, 3);
        warehouse.getInventoryItems().add(item);

        when(warehouseRepository.existsById(warehouseId)).thenReturn(true);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

        warehouseService.removeBook(warehouseId, book, 3);

        assertTrue(warehouse.getInventoryItems().isEmpty());
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void getInventory_returnsCorrectMap() {
        warehouse.getInventoryItems().add(new InventoryItem(book, warehouse, 12));

        when(warehouseRepository.existsById(warehouseId)).thenReturn(true);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

        Map<Book, Integer> inventory = warehouseService.getInventory(warehouseId);

        assertEquals(1, inventory.size());
        assertEquals(12, inventory.get(book));
    }

    @Test
    void getLowStockBooks_returnsBooksBelowThreshold() {
        Book anotherBook = new Book();
        anotherBook.setId(UUID.randomUUID());
        anotherBook.setTitle("Java Concurrency");

        warehouse.getInventoryItems().add(new InventoryItem(book, warehouse, 4));
        warehouse.getInventoryItems().add(new InventoryItem(anotherBook, warehouse, 9));

        when(warehouseRepository.existsById(warehouseId)).thenReturn(true);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

        List<Book> result = warehouseService.getLowStockBooks(warehouseId, 5);

        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    void validateLocation_notExists_throwsLocationNotFoundException() {
        when(warehouseRepository.existsById(warehouseId)).thenReturn(false);

        assertThrows(LocationNotFoundException.class, () -> warehouseService.getInventory(warehouseId));
    }
}
