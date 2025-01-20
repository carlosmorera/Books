package com.wearedev.demo.infrastructure.adapter.in.web;

import com.wearedev.demo.application.exception.BookNotFoundException;
import com.wearedev.demo.application.exception.InvalidBookException;
import com.wearedev.demo.application.port.in.BookUseCase;
import com.wearedev.demo.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookUseCase bookUseCase;

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        if (book.getTitle() == null || book.getAuthor() == null) {
            throw new InvalidBookException("Both title and author must be provided.");
        }
        Book createdBook = bookUseCase.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookUseCase.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found."));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookUseCase.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        if (bookUseCase.getBookById(id).isEmpty()) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }
        bookUseCase.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookById(
            @PathVariable Long id,
            @RequestBody Book updatedBook
    ) {
        if (bookUseCase.getBookById(id).isEmpty()) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }
        if (updatedBook.getTitle() == null || updatedBook.getAuthor() == null) {
            throw new InvalidBookException("Both title and author must be provided for updating.");
        }
        Book book = bookUseCase.updateBook(id, updatedBook);
        return ResponseEntity.ok(book);
    }
}