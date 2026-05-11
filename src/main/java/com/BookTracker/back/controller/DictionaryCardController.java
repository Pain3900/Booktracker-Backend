package com.BookTracker.back.controller;

import com.BookTracker.back.dto.CardRequest;
import com.BookTracker.back.dto.CardResponse;
import com.BookTracker.back.service.DictionaryCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books/{bookId}/cards")
@RequiredArgsConstructor
public class DictionaryCardController {

    private final DictionaryCardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> addCard(
            @PathVariable Long bookId,
            @RequestBody CardRequest request,
            Principal principal) {
        return ResponseEntity.ok(cardService.addCard(bookId, request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getCards(
            @PathVariable Long bookId,
            Principal principal) {
        return ResponseEntity.ok(cardService.getCardsByBook(bookId, principal.getName()));
    }
}