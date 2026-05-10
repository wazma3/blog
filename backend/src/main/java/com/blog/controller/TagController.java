package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.ArticleDTO;
import com.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public Result<List<ArticleDTO.TagDTO>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }
}
