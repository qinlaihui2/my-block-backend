package com.bookstore.blog.config;

import com.bookstore.blog.entity.Post;
import com.bookstore.blog.entity.User;
import com.bookstore.blog.repository.PostRepository;
import com.bookstore.blog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private static final String LEGACY_ADMIN_USERNAME = "admin";

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${blog.admin.username:caixukun}")
    private String adminUsername;

    @Value("${blog.admin.initial-password:2005ZWDZJS}")
    private String adminInitialPassword;

    public DataInitializer(UserRepository userRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User user = ensureAdminUser();
        if (user == null) {
            log.warn("未初始化管理员：请检查 blog.admin 配置（用户名与 initial-password 不能为空）。");
            return;
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

    private User ensureAdminUser() {
        Optional<User> primary = userRepository.findByUsername(adminUsername);
        if (primary.isPresent()) {
            return primary.get();
        }

        Optional<User> legacyAdmin = userRepository.findByUsername(LEGACY_ADMIN_USERNAME);
        if (legacyAdmin.isPresent()) {
            if (adminInitialPassword == null || adminInitialPassword.isBlank()) {
                return null;
            }
            User u = legacyAdmin.get();
            u.setUsername(adminUsername);
            u.setPassword(passwordEncoder.encode(adminInitialPassword));
            log.info("已将旧用户 {} 迁移为「{}」并重置密码", LEGACY_ADMIN_USERNAME, adminUsername);
            return userRepository.save(u);
        }

        if (adminInitialPassword == null || adminInitialPassword.isBlank()) {
            return null;
        }
        User u = new User();
        u.setUsername(adminUsername);
        u.setPassword(passwordEncoder.encode(adminInitialPassword));
        u.setRole("ROLE_ADMIN");
        log.info("已创建管理员用户「{}」（首次初始化）", adminUsername);
        return userRepository.save(u);
    }
}
