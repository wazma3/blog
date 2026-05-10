package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.ArticleDTO;
import com.blog.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public Result<Page<ArticleDTO.ArticleResponse>> getArticles(ArticleDTO.ArticlePageRequest request) {
        return Result.success(articleService.getPublishedArticles(request));
    }

    @GetMapping("/{id}")
    public Result<ArticleDTO.ArticleResponse> getArticle(@PathVariable Long id) {
        return Result.success(articleService.getArticleById(id));
    }
}
