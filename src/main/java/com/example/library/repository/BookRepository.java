package com.example.library.repository;

import com.example.library.entity.Book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAll();

    List<Book> findAllByAuthor_Id(Long authorId);

    List<Book> findAllByCustomer_Id(Long userId);

    Book getById(Long bookId);
}
