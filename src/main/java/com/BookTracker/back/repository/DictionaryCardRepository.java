package com.BookTracker.back.repository;

import com.BookTracker.back.model.entity.DictionaryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryCardRepository extends JpaRepository<DictionaryCard, Long> {

    // Получить все карточки, привязанные к конкретной книге
    List<DictionaryCard> findAllByBookId(Long bookId);

    // Поиск конкретного слова в словаре конкретной книги
    List<DictionaryCard> findByBookIdAndTermContainingIgnoreCase(Long bookId, String term);
}