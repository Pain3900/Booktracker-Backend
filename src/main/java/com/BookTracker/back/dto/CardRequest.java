package com.BookTracker.back.dto;

import lombok.Data;

@Data
public class CardRequest {
    private String term;
    private String definition;
    private String context;
}