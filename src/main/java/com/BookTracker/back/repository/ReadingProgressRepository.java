package com.BookTracker.back.repository;

import com.BookTracker.back.model.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
}