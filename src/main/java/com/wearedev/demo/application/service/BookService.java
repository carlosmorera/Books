package com.wearedev.demo.application.service;

import com.wearedev.demo.application.port.in.BookUseCase;
import com.wearedev.demo.application.port.out.BookRepository;
import com.wearedev.demo.domain.Book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService implements BookUseCase {

    private final BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.updateById(id, updatedBook);
    }
}