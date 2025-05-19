package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.dto.BookDTO;
import com.bookstore.mapper.BookMapper;
import com.bookstore.services.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE', 'WAREHOUSE')")
    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE', 'WAREHOUSE')")
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable UUID id) {
        return bookService.findBookById(id)
                .map(BookMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public void addBook(@RequestBody BookDTO dto) {
        Book book = new Book(dto.getTitle(), dto.getAuthor(), dto.getGenre(), dto.getPrice(), dto.getPublicationYear());
        bookService.addBook(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable UUID id) {
        bookService.removeBook(id);
    }
}
