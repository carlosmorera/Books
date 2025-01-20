package com.wearedev.demo.application.port.out;

import com.wearedev.demo.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    void deleteById(Long id);
    Book updateById(Long id, Book updatedBook);
}