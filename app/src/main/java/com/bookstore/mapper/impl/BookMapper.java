package com.bookstore.mapper.impl;

import com.bookstore.domain.Book;
import com.bookstore.dto.BookDTO;
import java.util.UUID;

import com.bookstore.mapper.api.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements GenericMapper<BookDTO, Book> {
    @Override
    public BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setGenre(book.getGenre());
        dto.setPrice(book.getPrice());
        dto.setPublicationYear(book.getPublicationYear());
        return dto;
    }

    @Override
    public Book fromDTO(BookDTO dto) {
        if (dto == null) return null;

        UUID id = dto.getId() != null ? dto.getId() : UUID.randomUUID();
        return new Book(
                id,
                dto.getTitle(),
                dto.getAuthor(),
                dto.getGenre(),
                dto.getPrice(),
                dto.getPublicationYear()
        );
    }
}
