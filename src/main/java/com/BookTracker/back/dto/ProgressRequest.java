package com.BookTracker.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProgressRequest {
    @JsonProperty("page")
    private Integer page;
}