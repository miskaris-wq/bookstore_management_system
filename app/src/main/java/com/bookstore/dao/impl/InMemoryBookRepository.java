package com.bookstore.dao.impl;

import com.bookstore.dao.api.BookRepository;
import com.bookstore.dao.impl.utils.impl.BookFinder;
import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.Book;

import java.util.List;
import java.util.UUID;

public class InMemoryBookRepository implements BookRepository {

    @Override
    public void add(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        DataSource.addBook(book.getId(), book);
    }

    @Override
    public boolean deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        return DataSource.deleteBook(id);
    }

    @Override
    public List<Book> findById(UUID id) {
        return newFinder().withId(id).find();
    }

    @Override
    public List<Book> findAll() {
        return List.copyOf(DataSource.getBooks().values());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return newFinder().withAuthor(author).find();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return newFinder().withTitle(title).find();
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return newFinder().withGenre(genre).find();
    }

    @Override
    public BookFinder newFinder() {
        return new BookFinder();
    }
}
