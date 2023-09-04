package com.example.study.persistence;

import com.example.study.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {
    List<BookEntity> findByUserId(String userId);
    List<BookEntity> findByTitle(String title);
}
