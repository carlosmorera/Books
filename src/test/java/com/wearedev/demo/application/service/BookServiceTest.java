package com.wearedev.demo.application.service;

import com.wearedev.demo.application.port.out.BookRepository;
import com.wearedev.demo.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "Mockito in Action", "Author Test");
    }

    @Test
    void addBook_shouldSaveAndReturnBook() {
        // Given
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        Book result = bookService.addBook(new Book(null, "Mockito in Action", "Author Test"));

        // Then
        assertEquals("Mockito in Action", result.getTitle());
        assertEquals("Author Test", result.getAuthor());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getAllBooks_shouldReturnListOfBooks() {
        // Given
        List<Book> books = Arrays.asList(
                new Book(1L, "Book 1", "Author 1"),
                new Book(2L, "Book 2", "Author 2")
        );
        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<Book> result = bookService.getAllBooks();

        // Then
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_shouldReturnBookWhenExists() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When
        Optional<Book> result = bookService.getBookById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Mockito in Action", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_shouldReturnEmptyWhenBookDoesNotExist() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Book> result = bookService.getBookById(999L);

        // Then
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findById(999L);
    }

    @Test
    void deleteBookById_shouldDeleteBook() {
        // When
        bookService.deleteBookById(1L);

        // Then
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateBook_shouldReturnUpdatedBook() {
        // Given
        Book updatedBook = new Book(1L, "Updated Title", "Updated Author");
        when(bookRepository.updateById(eq(1L), any(Book.class))).thenReturn(updatedBook);

        // When
        Book result = bookService.updateBook(1L, updatedBook);

        // Then
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        verify(bookRepository, times(1)).updateById(eq(1L), any(Book.class));
    }
}