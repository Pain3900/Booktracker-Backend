package com.BookTracker.back.controller;

import com.BookTracker.back.dto.ProgressRequest;
import com.BookTracker.back.dto.ProgressResponse;
import com.BookTracker.back.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    // Сохранить прогресс для конкретной книги
    @PostMapping("/books/{bookId}/progress")
    public ResponseEntity<ProgressResponse> recordProgress(
            @PathVariable Long bookId,
            @RequestBody ProgressRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();
        ProgressResponse response = progressService.recordProgress(bookId, request, userEmail);
        return ResponseEntity.ok(response);
    }

    // Получить историю всего прогресса пользователя
    @GetMapping("/progress")
    public ResponseEntity<List<ProgressResponse>> getProgress(Authentication authentication) {
        String userEmail = authentication.getName();
        List<ProgressResponse> progressList = progressService.getProgressByUser(userEmail);
        return ResponseEntity.ok(progressList);
    }
}