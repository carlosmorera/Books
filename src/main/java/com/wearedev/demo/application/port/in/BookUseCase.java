package com.wearedev.demo.application.port.in;

import com.wearedev.demo.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookUseCase {
    Book addBook(Book book);
    Optional<Book> getBookById(Long id);
    List<Book> getAllBooks();
    void deleteBookById(Long id);
    Book updateBook(Long id, Book updatedBook);
}