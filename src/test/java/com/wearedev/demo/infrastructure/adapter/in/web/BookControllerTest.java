package com.wearedev.demo.infrastructure.adapter.in.web;

import com.wearedev.demo.application.exception.BookNotFoundException;
import com.wearedev.demo.application.port.in.BookUseCase;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookUseCase bookUseCase;

    @InjectMocks
    private BookController bookController;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "Mockito in Action", "Author Test");
    }

    @Test
    void addBook_shouldReturnSavedBook() {
        // Given
        when(bookUseCase.addBook(any(Book.class))).thenReturn(book);

        // When
        Book result = bookController.addBook(new Book(null, "Mockito in Action", "Author Test")).getBody();

        // Then
        assertEquals("Mockito in Action", result.getTitle());
        assertEquals("Author Test", result.getAuthor());
        verify(bookUseCase, times(1)).addBook(any(Book.class));
    }

    @Test
    void getBookById_shouldReturnBookWhenExists() {
        // Given
        when(bookUseCase.getBookById(1L)).thenReturn(Optional.of(book));

        // When
        Book result = bookController.getBookById(1L).getBody();

        // Then
        assertEquals("Mockito in Action", result.getTitle());
        assertEquals("Author Test", result.getAuthor());
        verify(bookUseCase, times(1)).getBookById(1L);
    }

    @Test
    void getBookById_shouldThrowExceptionWhenBookDoesNotExist() {
        // Given
        when(bookUseCase.getBookById(999L)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(BookNotFoundException.class, () -> {
            bookController.getBookById(999L);
        });

        assertEquals("Book with ID 999 not found.", exception.getMessage());
        verify(bookUseCase, times(1)).getBookById(999L);
    }
    @Test
    void getAllBooks_shouldReturnListOfBooks() {
        // Given
        List<Book> books = Arrays.asList(
                new Book(1L, "Book 1", "Author 1"),
                new Book(2L, "Book 2", "Author 2")
        );
        when(bookUseCase.getAllBooks()).thenReturn(books);

        // When
        List<Book> result = bookController.getAllBooks().getBody();

        // Then
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
        verify(bookUseCase, times(1)).getAllBooks();
    }

    @Test
    void deleteBookById_shouldCallDeleteMethod() {
        // Given
        when(bookUseCase.getBookById(1L)).thenReturn(Optional.of(new Book(1L, "Test Title", "Test Author")));
        doNothing().when(bookUseCase).deleteBookById(1L);

        // When
        bookController.deleteBookById(1L);

        // Then
        verify(bookUseCase, times(1)).getBookById(1L);
        verify(bookUseCase, times(1)).deleteBookById(1L);
    }

    @Test
    void updateBookById_shouldReturnUpdatedBook() {
        // Given
        Book existingBook = new Book(1L, "Original Title", "Original Author");
        Book updatedBook = new Book(1L, "Updated Title", "Updated Author");

        when(bookUseCase.getBookById(1L)).thenReturn(Optional.of(existingBook));
        when(bookUseCase.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        // When
        Book result = bookController.updateBookById(1L, updatedBook).getBody();

        // Then
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        verify(bookUseCase, times(1)).getBookById(1L);
        verify(bookUseCase, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void updateBookById_shouldThrowExceptionWhenBookDoesNotExist() {
        // Given
        Book updatedBook = new Book(1L, "Updated Title", "Updated Author");
        when(bookUseCase.getBookById(1L)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(BookNotFoundException.class, () -> {
            bookController.updateBookById(1L, updatedBook);
        });

        assertEquals("Book with ID 1 not found.", exception.getMessage());
        verify(bookUseCase, times(1)).getBookById(1L);
        verify(bookUseCase, never()).updateBook(eq(1L), any(Book.class));
    }
}