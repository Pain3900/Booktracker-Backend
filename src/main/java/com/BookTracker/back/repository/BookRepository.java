package com.BookTracker.back.repository;

import com.BookTracker.back.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Получить все книги, принадлежащие конкретному пользователю
    List<Book> findAllByUserId(Long userId);

    // Найти книги пользователя по названию (например, для поиска с игнорированием регистра)
    List<Book> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);
}