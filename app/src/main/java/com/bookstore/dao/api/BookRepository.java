package com.bookstore.dao.api;

import com.bookstore.dao.api.common.SearchableRepository;
import com.bookstore.dao.impl.utils.impl.BookFinder;
import com.bookstore.domain.Book;

import java.util.List;

public interface BookRepository extends SearchableRepository<Book, BookFinder> {
    List<Book> findByAuthor(String author);
    List<Book> findByTitle(String title);
    List<Book> findByGenre(String genre);
}
