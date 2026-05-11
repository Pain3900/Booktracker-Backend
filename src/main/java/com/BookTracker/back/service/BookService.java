package com.BookTracker.back.service;

import com.BookTracker.back.dto.BookRequest;
import com.BookTracker.back.dto.BookResponse;
import com.BookTracker.back.model.entity.Book;
import com.BookTracker.back.model.entity.User;
import com.BookTracker.back.repository.BookRepository;
import com.BookTracker.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookResponse createBook(BookRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
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

        // Проверка: принадлежит ли книга этому пользователю?
        if (!book.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Нет прав для удаления этой книги");
        }

        bookRepository.delete(book);
    }

    // Вспомогательный метод для конвертации Entity в DTO
    private BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .createdAt(book.getCreatedAt())
                .build();
    }
}
