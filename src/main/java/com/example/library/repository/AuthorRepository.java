package com.example.library.repository;

import com.example.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author getById(Long authorId);
}
