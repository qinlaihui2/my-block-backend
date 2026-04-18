package com.bookstore.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank @Size(max = 120) String title,
        @Size(max = 280) String summary,
        @NotBlank String content,
        Boolean published
) {
}
