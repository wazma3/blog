package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.ArticleDTO;
import com.blog.service.ArticleService;
import com.blog.service.TagService;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ArticleService articleService;
    private final TagService tagService;

    @GetMapping("/articles")
    public Result<Page<ArticleDTO.ArticleResponse>> getArticles(ArticleDTO.ArticlePageRequest request) {
        return Result.success(articleService.getAllArticles(request));
    }

    @PostMapping("/articles")
    public Result<Long> createArticle(@Valid @RequestBody ArticleDTO.CreateRequest request,
                                      Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(articleService.createArticle(request, userId));
    }

    @PutMapping("/articles/{id}")
    public Result<Void> updateArticle(@PathVariable Long id,
                                      @RequestBody ArticleDTO.UpdateRequest request,
                                      Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        articleService.updateArticle(id, request, userId);
        return Result.success();
    }

    @DeleteMapping("/articles/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        articleService.deleteArticle(id, userId);
        return Result.success();
    }

    @PostMapping("/tags")
    public Result<Long> createTag(@RequestParam String name) {
        return Result.success(tagService.createTag(name));
    }

    @DeleteMapping("/tags/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
}
