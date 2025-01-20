package com.wearedev.demo.infrastructure.adapter.out.db;

import com.wearedev.demo.application.port.out.BookRepository;
import com.wearedev.demo.domain.Book;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaBookRepository.class);

    private final SpringDataBookRepository springDataBookRepository;

    @Override
    public Book save(Book book) {
        logger.info("Saving book: {}", book);
        BookEntity savedEntity = springDataBookRepository.save(BookEntity.fromDomain(book));
        Book result = savedEntity.toDomain();
        logger.info("Book saved successfully: {}", result);
        return result;
    }

    @Override
    public Optional<Book> findById(Long id) {
        logger.info("Finding book by ID: {}", id);
        Optional<BookEntity> optionalEntity = springDataBookRepository.findById(id);
        if (optionalEntity.isPresent()) {
            logger.info("Book found: {}", optionalEntity.get());
        } else {
            logger.warn("No book found with ID: {}", id);
        }
        return optionalEntity.map(BookEntity::toDomain);
    }

    @Override
    public List<Book> findAll() {
        logger.info("Retrieving all books");
        List<BookEntity> entities = springDataBookRepository.findAll();
        List<Book> result = entities.stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
        logger.info("Total books retrieved: {}", result.size());
        return result;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting book by ID: {}", id);
        try {
            springDataBookRepository.deleteById(id);
            logger.info("Book with ID: {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting book with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public Book updateById(Long id, Book updatedBook) {
        logger.info("Updating book with ID: {}. New data: {}", id, updatedBook);
        Optional<BookEntity> optionalEntity = springDataBookRepository.findById(id);

        if (optionalEntity.isEmpty()) {
            logger.error("Book with ID: {} not found for update", id);
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }

        BookEntity existingEntity = optionalEntity.get();
        logger.info("Current book data: {}", existingEntity);

        existingEntity.setTitle(updatedBook.getTitle());
        existingEntity.setAuthor(updatedBook.getAuthor());

        BookEntity updatedEntity = springDataBookRepository.save(existingEntity);
        Book result = updatedEntity.toDomain();

        logger.info("Book with ID: {} updated successfully. Updated data: {}", id, result);
        return result;
    }
}