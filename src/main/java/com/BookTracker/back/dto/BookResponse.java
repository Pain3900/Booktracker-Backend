package com.BookTracker.back.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String description;
    private LocalDateTime createdAt;
}