package com.BookTracker.back.repository;

import com.BookTracker.back.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA автоматически сгенерирует SQL: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Проверка, существует ли пользователь с таким email (удобно при регистрации)
    boolean existsByEmail(String email);
}