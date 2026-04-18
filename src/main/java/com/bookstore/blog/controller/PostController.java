package com.bookstore.blog.controller;

import com.bookstore.blog.common.Result;
import com.bookstore.blog.dto.PostRequest;
import com.bookstore.blog.dto.PostResponse;
import com.bookstore.blog.entity.Post;
import com.bookstore.blog.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public Result<List<PostResponse>> list() {
        return Result.ok(postRepository.findAll().stream().map(PostResponse::from).toList());
    }

    @GetMapping("/{id}")
    public Result<PostResponse> detail(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(PostResponse::from)
                .map(Result::ok)
                .orElseGet(() -> Result.fail(40404, "文章不存在"));
    }

    @PostMapping
    public ResponseEntity<Result<PostResponse>> create(@Valid @RequestBody PostRequest request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setSummary(request.summary());
        post.setContent(request.content());
        post.setPublished(Boolean.TRUE.equals(request.published()));
        Post saved = postRepository.save(post);
        return ResponseEntity.ok(Result.ok("创建成功", PostResponse.from(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<PostResponse>> update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(request.title());
                    post.setSummary(request.summary());
                    post.setContent(request.content());
                    post.setPublished(Boolean.TRUE.equals(request.published()));
                    Post saved = postRepository.save(post);
                    return ResponseEntity.ok(Result.ok("更新成功", PostResponse.from(saved)));
                })
                .orElseGet(() -> ResponseEntity.status(404).body(Result.fail(40404, "文章不存在")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Result.fail(40404, "文章不存在"));
        }
        postRepository.deleteById(id);
        return ResponseEntity.ok(Result.ok("删除成功", null));
    }
}
