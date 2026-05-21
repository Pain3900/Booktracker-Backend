package com.BookTracker.back.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ProgressResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Integer pageCount;
    private Integer page;
    private LocalDateTime recordedAt;
}