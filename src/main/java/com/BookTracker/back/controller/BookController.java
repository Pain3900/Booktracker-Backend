package com.BookTracker.back.controller;

import com.BookTracker.back.dto.BookRequest;
import com.BookTracker.back.dto.BookResponse;
import com.BookTracker.back.dto.ProgressRequest;
import com.BookTracker.back.dto.ProgressResponse;
import com.BookTracker.back.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import java.security.Principal;
import java.util.List;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request, Principal principal) {
        return ResponseEntity.ok(bookService.createBook(request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(Principal principal) {
        return ResponseEntity.ok(bookService.getAllUserBooks(principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id, Principal principal) {
        bookService.deleteBook(id, principal.getName());
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();
        return ResponseEntity.ok(bookService.updateBook(id, request, userEmail));
    }
}
