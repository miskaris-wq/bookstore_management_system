package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.dto.BookDTO;
import com.bookstore.mapper.impl.BookMapper;
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
    private final BookMapper bookMapper;

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";

    private static final String ANY_AUTHENTICATED =
            "hasAnyRole('" + ROLE_USER + "','" + ROLE_ADMIN + "','" + ROLE_MANAGER + "','" + ROLE_EMPLOYEE + "')";
    private static final String ADMIN_ONLY = "hasRole('" + ROLE_ADMIN + "')";

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PreAuthorize(ANY_AUTHENTICATED)
    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize(ANY_AUTHENTICATED)
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable UUID id) {
        return bookService.findBookById(id)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    @PreAuthorize(ADMIN_ONLY)
    @PostMapping
    public void addBook(@RequestBody BookDTO dto) {
        Book book = new Book(dto.getTitle(), dto.getAuthor(), dto.getGenre(), dto.getPrice(), dto.getPublicationYear());
        bookService.addBook(book);
    }

    @PreAuthorize(ADMIN_ONLY)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable UUID id) {
        bookService.removeBook(id);
    }
}