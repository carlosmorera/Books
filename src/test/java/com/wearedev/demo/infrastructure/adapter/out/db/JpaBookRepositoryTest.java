package com.wearedev.demo.infrastructure.adapter.out.db;

import com.wearedev.demo.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaBookRepositoryTest {

    @Mock
    private SpringDataBookRepository springDataBookRepository;

    @InjectMocks
    private JpaBookRepository jpaBookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldSaveBook() {
        // Given
        BookEntity entity = new BookEntity();
        entity.setTitle("Mockito in Action");
        entity.setAuthor("Author Test");
        Book book = new Book(1L, "Mockito in Action", "Author Test");


        when(springDataBookRepository.save(any(BookEntity.class))).thenReturn(entity);

        // When
        Book result = jpaBookRepository.save(book);

        // Then
        assertNotNull(result);
        assertEquals("Mockito in Action", result.getTitle());
        assertEquals("Author Test", result.getAuthor());
        verify(springDataBookRepository, times(1)).save(any(BookEntity.class));
    }

    @Test
    void findById_shouldReturnBook() {
        // Given
        BookEntity entity = new BookEntity();
        entity.setTitle("Mockito in Action");
        entity.setAuthor("Author Test");
        when(springDataBookRepository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        Optional<Book> result = jpaBookRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Mockito in Action", result.get().getTitle());
        verify(springDataBookRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        // Given
        BookEntity entity1 = new BookEntity();
        entity1.setTitle("Book 1");
        entity1.setAuthor("Author 1");

        BookEntity entity2 = new BookEntity();
        entity2.setTitle("Book 2");
        entity2.setAuthor("Author 2");


        List<BookEntity> entities = Arrays.asList(entity1,entity2);
        when(springDataBookRepository.findAll()).thenReturn(entities);

        // When
        List<Book> result = jpaBookRepository.findAll();

        // Then
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        verify(springDataBookRepository, times(1)).findAll();
    }

    @Test
    void deleteById_shouldCallDeleteMethod() {
        // Given
        Long id = 1L;

        // When
        jpaBookRepository.deleteById(id);

        // Then
        verify(springDataBookRepository, times(1)).deleteById(id);
    }

    @Test
    void updateById_shouldUpdateBook() {
        // Given
        Long id = 1L;
        Book updatedBook = new Book(id, "Updated Title", "Updated Author");

        BookEntity existingEntity = new BookEntity();
        existingEntity.setTitle("Book 1");
        existingEntity.setAuthor("Author 1");

        BookEntity updatedEntity = new BookEntity();
        updatedEntity.setTitle("Book 2");
        updatedEntity.setAuthor("Author 2");

        when(springDataBookRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(springDataBookRepository.save(existingEntity)).thenReturn(updatedEntity);

        // When
        Book result = jpaBookRepository.updateById(id, updatedBook);

        // Then
        assertNotNull(result);
        assertEquals("Book 2", result.getTitle());
        assertEquals("Author 2", result.getAuthor());
        verify(springDataBookRepository, times(1)).findById(id);
        verify(springDataBookRepository, times(1)).save(existingEntity);
    }

    @Test
    void updateById_shouldThrowExceptionIfNotFound() {
        // Given
        Long id = 999L;
        Book updatedBook = new Book(id, "Updated Title", "Updated Author");
        when(springDataBookRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jpaBookRepository.updateById(id, updatedBook)
        );

        assertEquals("Book with ID 999 not found", exception.getMessage());
        verify(springDataBookRepository, times(1)).findById(id);
        verify(springDataBookRepository, never()).save(any(BookEntity.class));
    }
}
