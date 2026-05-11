package com.BookTracker.back.service;

import com.BookTracker.back.dto.CardRequest;
import com.BookTracker.back.dto.CardResponse;
import com.BookTracker.back.model.entity.Book;
import com.BookTracker.back.model.entity.DictionaryCard;
import com.BookTracker.back.repository.BookRepository;
import com.BookTracker.back.repository.DictionaryCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryCardService {

    private final DictionaryCardRepository cardRepository;
    private final BookRepository bookRepository;

    public CardResponse addCard(Long bookId, CardRequest request, String userEmail) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // Проверка прав
        if (!book.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Нет прав для добавления карточки в эту книгу");
        }

        DictionaryCard card = new DictionaryCard();
        card.setTerm(request.getTerm());
        card.setDefinition(request.getDefinition());
        card.setContext(request.getContext());
        card.setBook(book);

        DictionaryCard savedCard = cardRepository.save(card);
        return mapToResponse(savedCard);
    }

    public List<CardResponse> getCardsByBook(Long bookId, String userEmail) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (!book.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Доступ запрещен");
        }

        return cardRepository.findAllByBookId(bookId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CardResponse mapToResponse(DictionaryCard card) {
        return CardResponse.builder()
                .id(card.getId())
                .term(card.getTerm())
                .definition(card.getDefinition())
                .context(card.getContext())
                .createdAt(card.getCreatedAt())
                .build();
    }
}