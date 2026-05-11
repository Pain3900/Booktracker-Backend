package com.BookTracker.back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dictionary_cards")
@Getter
@Setter
@NoArgsConstructor
public class DictionaryCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String definition;

    @Column(columnDefinition = "TEXT")
    private String context;

    // Карточка всегда привязана к одной книге
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}