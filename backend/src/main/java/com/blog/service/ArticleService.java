package com.blog.service;

import com.blog.dto.ArticleDTO;
import com.blog.entity.Article;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.repository.ArticleRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createArticle(ArticleDTO.CreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCoverImage(request.getCoverImage());
        article.setUser(user);
        if (request.getStatus() != null) {
            article.setStatus(request.getStatus());
        }

        if (request.getTagIds() != null) {
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new RuntimeException("标签不存在: " + tagId)))
                    .collect(Collectors.toSet());
            article.setTags(tags);
        }

        articleRepository.save(article);
        return article.getId();
    }

    @Transactional
    public void updateArticle(Long id, ArticleDTO.UpdateRequest request, Long userId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        if (!article.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权修改此文章");
        }

        if (request.getTitle() != null) article.setTitle(request.getTitle());
        if (request.getContent() != null) article.setContent(request.getContent());
        if (request.getSummary() != null) article.setSummary(request.getSummary());
        if (request.getCoverImage() != null) article.setCoverImage(request.getCoverImage());
        if (request.getStatus() != null) article.setStatus(request.getStatus());

        if (request.getTagIds() != null) {
            Set<Tag> tags = request.getTagIds().stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new RuntimeException("标签不存在: " + tagId)))
                    .collect(Collectors.toSet());
            article.setTags(tags);
        }

        articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long id, Long userId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        if (!article.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除此文章");
        }

        articleRepository.delete(article);
    }

    public ArticleDTO.ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);

        return toResponse(article);
    }

    public Page<ArticleDTO.ArticleResponse> getPublishedArticles(ArticleDTO.ArticlePageRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1, request.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Article> page;
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            page = articleRepository.searchByKeyword(request.getKeyword(), 1, pageable);
        } else if (request.getTagId() != null) {
            page = articleRepository.findByTagIdAndStatus(request.getTagId(), 1, pageable);
        } else {
            page = articleRepository.findByStatus(1, pageable);
        }

        return page.map(this::toResponse);
    }

    public Page<ArticleDTO.ArticleResponse> getAllArticles(ArticleDTO.ArticlePageRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1, request.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Article> page = articleRepository.findAllByOrderByCreatedAtDesc(pageable);

        return page.map(this::toResponse);
    }

    private ArticleDTO.ArticleResponse toResponse(Article article) {
        ArticleDTO.ArticleResponse resp = new ArticleDTO.ArticleResponse();
        resp.setId(article.getId());
        resp.setTitle(article.getTitle());
        resp.setContent(article.getContent());
        resp.setSummary(article.getSummary());
        resp.setCoverImage(article.getCoverImage());
        resp.setStatus(article.getStatus());
        resp.setViewCount(article.getViewCount());
        resp.setAuthorName(article.getUser().getUsername());
        resp.setAuthorId(article.getUser().getId());
        resp.setCreatedAt(article.getCreatedAt() != null ? article.getCreatedAt().toString() : null);
        resp.setUpdatedAt(article.getUpdatedAt() != null ? article.getUpdatedAt().toString() : null);

        if (article.getTags() != null) {
            resp.setTags(article.getTags().stream().map(tag -> {
                ArticleDTO.TagDTO tagDTO = new ArticleDTO.TagDTO();
                tagDTO.setId(tag.getId());
                tagDTO.setName(tag.getName());
                return tagDTO;
            }).collect(Collectors.toSet()));
        }

        return resp;
    }
}
