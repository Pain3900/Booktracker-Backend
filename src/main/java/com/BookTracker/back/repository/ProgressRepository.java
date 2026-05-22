package com.BookTracker.back.repository;

import com.BookTracker.back.model.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<ReadingProgress, Long> {
    // Этот метод нужен сервису для поиска истории по email пользователя
    List<ReadingProgress> findByBookUserEmailOrderByRecordedAtDesc(String email);
}