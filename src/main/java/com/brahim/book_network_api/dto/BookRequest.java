package com.brahim.book_network_api.dto;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        Long id,
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Author Name is required") String authorName,
        @NotBlank(message = "ISBN is required") String isbn,
        @NotBlank(message = "Synopsis is required") String synopsis,
        boolean shareable) {
}
