package com.BookTracker.back.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CardResponse {
    private Long id;
    private String term;
    private String definition;
    private String context;
    private LocalDateTime createdAt;
}