package com.wearedev.demo.infrastructure.config;

import com.wearedev.demo.application.service.BookService;
import com.wearedev.demo.application.port.out.BookRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }
}