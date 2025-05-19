package com.bookstore.dao.impl.utils.impl;

import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.Book;
import java.util.List;
import java.util.UUID;

public class BookFinder extends AbstractEntityFinder<Book> {
    @Override
    protected List<Book> getDataSource() {
        return DataSource.getBooks().values().stream().toList();
    }

    public BookFinder withAuthor(String author) {
        addFilter(book -> author == null || containsIgnoreCase(book.getAuthor(), author));
        return this;
    }

    public BookFinder withTitle(String title) {
        addFilter(book -> title == null || containsIgnoreCase(book.getTitle(), title));
        return this;
    }

    public BookFinder withGenre(String genre) {
        addFilter(book -> genre == null || containsIgnoreCase(book.getGenre(), genre));
        return this;
    }

    public BookFinder withId(UUID id) {
        addFilter(book -> id == null || book.getId().equals(id));
        return this;
    }
}
