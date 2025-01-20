package com.wearedev.demo.infrastructure.adapter.out.db;

import com.wearedev.demo.domain.Book;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "BOOK")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "AUTHOR", nullable = false)
    private String author;


    public static BookEntity fromDomain(Book book) {
        BookEntity entity = new BookEntity();
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        return entity;
    }

    public Book toDomain() {
        return new Book(this.id, this.title, this.author);
    }
}