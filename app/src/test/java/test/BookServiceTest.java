package test;

import com.bookstore.repository.JpaBookRepository;
import com.bookstore.domain.Book;
import com.bookstore.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private JpaBookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        book = new Book(
                bookId,
                "Effective Java",
                "Joshua Bloch",
                "Programming",
                BigDecimal.valueOf(29.99),
                2018
        );
    }

    @Test
    void addBook_ShouldCallRepositorySave() {
        bookService.addBook(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void addBook_ShouldThrowException_WhenBookIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.addBook(null));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void findBookById_ShouldReturnBook_WhenBookExists() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Optional<Book> result = bookService.findBookById(bookId);
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @Test
    void findBookById_ShouldReturnEmpty_WhenBookDoesNotExist() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Optional<Book> result = bookService.findBookById(bookId);
        assertTrue(result.isEmpty());
    }

    @Test
    void groupBooksByGenre_ShouldReturnGroupedBooks() {
        Book book1 = new Book(UUID.randomUUID(), "Book 1", "Author 1", "Fantasy", BigDecimal.TEN, 2020);
        Book book2 = new Book(UUID.randomUUID(), "Book 2", "Author 2", "Fantasy", BigDecimal.TEN, 2021);
        Book book3 = new Book(UUID.randomUUID(), "Book 3", "Author 3", "Sci-Fi", BigDecimal.TEN, 2022);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2, book3));

        Map<String, List<Book>> groupedBooks = bookService.groupBooksByGenre();


        assertNotNull(groupedBooks);
        assertTrue(groupedBooks.containsKey("fantasy"), "Key 'fantasy' absent");
        assertTrue(groupedBooks.containsKey("sci-fi"), "Key 'sci-fi' absent");
        assertEquals(2, groupedBooks.get("fantasy").size());
        assertEquals(1, groupedBooks.get("sci-fi").size());
    }


    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        List<Book> books = bookService.getAllBooks();
        assertEquals(1, books.size());
        assertEquals(book, books.getFirst());
    }

    @Test
    void removeBook_ShouldReturnTrue_WhenBookExists() {
        when(bookRepository.existsById(bookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(bookId);

        assertTrue(bookService.removeBook(bookId));
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void removeBook_ShouldReturnFalse_WhenBookDoesNotExist() {
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertFalse(bookService.removeBook(bookId));
        verify(bookRepository, never()).deleteById(bookId);
    }

    @Test
    void removeBook_ShouldThrowException_WhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> bookService.removeBook(null));
    }
}
