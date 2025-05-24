package com.bookstore.services;

import com.bookstore.repository.JpaBookRepository;
import com.bookstore.domain.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class BookService {

    private final JpaBookRepository bookRepository;

    public BookService(JpaBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        bookRepository.save(book);
    }

    public Optional<Book> findBookById(UUID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return bookRepository.findById(id);
    }

    public Map<String, List<Book>> groupBooksByGenre() {
        return bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(book -> book.getGenre().toLowerCase()));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public boolean removeBook(UUID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> findByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }
}
