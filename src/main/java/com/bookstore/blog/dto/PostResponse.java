package com.bookstore.blog.dto;

import com.bookstore.blog.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String summary,
        String content,
        Boolean published,
        LocalDateTime createdAt,
        Long version
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getContent(),
                post.getPublished(),
                post.getCreatedAt(),
                post.getVersion()
        );
    }
}
