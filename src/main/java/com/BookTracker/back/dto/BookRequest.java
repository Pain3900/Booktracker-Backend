package com.BookTracker.back.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String description;
    private Integer pageCount;
}
