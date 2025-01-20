package com.wearedev.demo.infrastructure.adapter.out.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBookRepository extends JpaRepository<BookEntity, Long> {
}