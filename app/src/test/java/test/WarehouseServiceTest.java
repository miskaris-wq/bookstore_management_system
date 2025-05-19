package test;

import com.bookstore.dao.api.WarehouseRepository;
import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.LocationNotFoundException;
import com.bookstore.services.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private InventoryHolder warehouseInventory;

    @InjectMocks
    private WarehouseService warehouseService;

    private Warehouse warehouse;
    private Book book;
    private UUID warehouseId;

    @BeforeEach
    void setUp() {
        warehouseId = UUID.randomUUID();
        warehouse = new Warehouse("123 Main St");
        book = new Book(
                UUID.randomUUID(),
                "Effective Java",
                "Joshua Bloch",
                "Programming",
                BigDecimal.valueOf(29.99),
                2018
        );
    }

    @Test
    void addBook_ShouldAddItemToInventory_WhenWarehouseExists() {
        when(warehouseRepository.findById(warehouseId)).thenReturn(List.of(warehouse));
        warehouseService.addBook(warehouseId, book, 10);
        verify(warehouseInventory, times(1)).addItem(book, 10);
    }

    @Test
    void addBook_ShouldThrowException_WhenWarehouseNotFound() {
        when(warehouseRepository.findById(warehouseId)).thenReturn(List.of());
        assertThrows(LocationNotFoundException.class,
                () -> warehouseService.addBook(warehouseId, book, 10));
    }

    @Test
    void addBook_ShouldThrowException_WhenQuantityIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> warehouseService.addBook(warehouseId, book, -1));
    }

    @Test
    void removeBook_ShouldRemoveItemFromInventory_WhenStockIsSufficient() throws InsufficientStockException {
        when(warehouseRepository.findById(warehouseId)).thenReturn(List.of(warehouse));
        doNothing().when(warehouseInventory).removeItem(book, 10);

        warehouseService.removeBook(warehouseId, book, 10);

        verify(warehouseInventory).removeItem(book, 10);
        verify(warehouseInventory, never()).getItemQuantity(book);
        verifyNoMoreInteractions(warehouseInventory);
    }

    @Test
    void removeBook_ShouldThrowException_WhenStockIsInsufficient() {
        when(warehouseRepository.findById(warehouseId)).thenReturn(List.of(warehouse));
        when(warehouseInventory.getItemQuantity(book)).thenReturn(5);
        doThrow(new IllegalStateException("Not enough stock"))
                .when(warehouseInventory).removeItem(book, 10);

        InsufficientStockException exception = assertThrows(InsufficientStockException.class,
                () -> warehouseService.removeBook(warehouseId, book, 10));

        assertEquals(book.getTitle(), exception.getBookTitle());
        assertEquals(5, exception.getAvailable());
        assertEquals(10, exception.getRequested());

        verify(warehouseInventory).getItemQuantity(book);
        verify(warehouseInventory).removeItem(book, 10);
    }

    @Test
    void getInventory_ShouldReturnInventory_WhenWarehouseExists() {
        when(warehouseRepository.findById(warehouseId)).thenReturn(List.of(warehouse));
        when(warehouseInventory.getInventory()).thenReturn(Map.of(book, 10));
        Map<Book, Integer> inventory = warehouseService.getInventory(warehouseId);
        assertEquals(1, inventory.size());
        assertEquals(10, inventory.get(book));
    }

    @Test
    void getLowStockBooks_ShouldReturnLowStockItems() {
        Book lowStockBook = new Book(
                UUID.randomUUID(),
                "Clean Code",
                "Robert Martin",
                "Programming",
                BigDecimal.valueOf(35.99),
                2008
        );
        when(warehouseRepository.findById(warehouseId)).thenReturn(List.of(warehouse));
        when(warehouseInventory.getInventory()).thenReturn(
                Map.of(book, 15, lowStockBook, 5));
        List<Book> lowStockBooks = warehouseService.getLowStockBooks(warehouseId, 10);
        assertEquals(1, lowStockBooks.size());
        assertEquals(lowStockBook, lowStockBooks.getFirst());
    }

    @Test
    void getLowStockBooks_ShouldThrowException_WhenThresholdIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> warehouseService.getLowStockBooks(warehouseId, -1));
    }
}
