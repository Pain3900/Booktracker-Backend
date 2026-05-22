package com.BookTracker.back.service;

import com.BookTracker.back.dto.ProgressRequest;
import com.BookTracker.back.dto.ProgressResponse;
import com.BookTracker.back.model.entity.Book;
import com.BookTracker.back.model.entity.ReadingProgress;
import com.BookTracker.back.repository.BookRepository;
import com.BookTracker.back.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ProgressResponse recordProgress(Long bookId, ProgressRequest request, String userEmail) {
        // 1. Находим книгу
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // 2. Проверяем, что книга принадлежит тому, кто делает запрос
        if (!book.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Нет доступа к этой книге");
        }

        // 3. Обновляем текущую страницу в самой книге
        book.setCurrentPage(request.getPage());
        bookRepository.save(book);

        // 4. Создаем запись в истории прогресса
        ReadingProgress progress = ReadingProgress.builder()
                .book(book)
                .page(request.getPage())
                .build();

        ReadingProgress savedProgress = progressRepository.save(progress);

        return mapToResponse(savedProgress);
    }

    public List<ProgressResponse> getProgressByUser(String userEmail) {
        return progressRepository.findByBookUserEmailOrderByRecordedAtDesc(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProgressResponse mapToResponse(ReadingProgress progress) {
        return ProgressResponse.builder()
                .id(progress.getId())
                .bookId(progress.getBook().getId())
                .bookTitle(progress.getBook().getTitle()) // Берем название из книги
                .pageCount(progress.getBook().getPageCount()) // Берем кол-во страниц из книги
                .page(progress.getPage())
                .recordedAt(progress.getRecordedAt())
                .build();
    }
}