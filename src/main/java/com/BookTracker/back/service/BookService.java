package com.BookTracker.back.service;

import com.BookTracker.back.dto.BookRequest;
import com.BookTracker.back.dto.BookResponse;
import com.BookTracker.back.dto.ProgressRequest;
import com.BookTracker.back.dto.ProgressResponse;
import com.BookTracker.back.model.entity.Book;
import com.BookTracker.back.model.entity.ReadingProgress;
import com.BookTracker.back.model.entity.Shelf;
import com.BookTracker.back.model.entity.User;
import com.BookTracker.back.repository.BookRepository;
import com.BookTracker.back.repository.ProgressRepository;
import com.BookTracker.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ProgressRepository readingProgressRepository;

    public BookResponse createBook(BookRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());

        if (request.getPageCount() != null) {
            book.setPageCount(request.getPageCount());
        }
        if (request.getShelf() != null && !request.getShelf().trim().isEmpty()) {
            try {
                book.setShelf(Shelf.valueOf(request.getShelf().toUpperCase()));
            } catch (IllegalArgumentException e) {
                book.setShelf(Shelf.PLAN);
            }
        } else {
            book.setShelf(Shelf.PLAN);
        }

        book.setUser(user);

        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public List<BookResponse> getAllUserBooks(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return bookRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteBook(Long bookId, String userEmail) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        if (!book.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Нет прав для удаления этой книги");
        }

        bookRepository.delete(book);
    }

    @Transactional
    public ProgressResponse saveProgress(Long bookId, ProgressRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        book.setCurrentPage(request.getPage());

        if (book.getPageCount() != null && book.getCurrentPage() >= book.getPageCount()) {
            book.setShelf(Shelf.FINISHED);
        } else {
            book.setShelf(Shelf.READING);
        }
        bookRepository.save(book);

        ReadingProgress progress = ReadingProgress.builder()
                .book(book)
                .page(request.getPage())
                .build();

        ReadingProgress savedProgress = readingProgressRepository.save(progress);

        return ProgressResponse.builder()
                .id(savedProgress.getId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .pageCount(book.getPageCount())
                .page(savedProgress.getPage())
                .recordedAt(savedProgress.getRecordedAt())
                .build();
    }

    private BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .pageCount(book.getPageCount())
                .currentPage(book.getCurrentPage())
                .shelf(book.getShelf() != null ? book.getShelf().name() : null)
                .createdAt(book.getCreatedAt())
                .build();
    }
}