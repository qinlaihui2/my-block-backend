package com.bookstore.blog.config;

import com.bookstore.blog.entity.User;
import com.bookstore.blog.entity.Post;
import com.bookstore.blog.repository.PostRepository;
import com.bookstore.blog.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User user;
        if (userRepository.findByUsername("admin").isEmpty()) {
            user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole("ROLE_ADMIN");
            user = userRepository.save(user);
        } else {
            user = userRepository.findByUsername("admin").orElseThrow();
        }

        if (postRepository.count() == 0) {
            Post first = new Post();
            first.setTitle("如何打造清新杂志风博客");
            first.setSummary("从留白、层级与字体组合出发，建立干净但有温度的阅读体验。");
            first.setContent("这是示例文章内容，可在后续替换为 Markdown 富文本内容。");
            first.setPublished(true);
            first.setAuthor(user);

            Post second = new Post();
            second.setTitle("Spring Boot + Next.js 联调清单");
            second.setSummary("认证、跨域、返回结构统一是联调稳定性的关键。");
            second.setContent("这是第二篇示例文章内容，用于验证首页卡片展示。");
            second.setPublished(true);
            second.setAuthor(user);

            postRepository.save(first);
            postRepository.save(second);
        }
    }
}
