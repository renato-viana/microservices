package com.udemy.microservice.bookservice.repository;

import com.udemy.microservice.bookservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
