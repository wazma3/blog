package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

public class ArticleDTO {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "文章标题不能为空")
        @Size(max = 200, message = "标题最长200字")
        private String title;

        @NotBlank(message = "文章内容不能为空")
        private String content;

        @Size(max = 500, message = "摘要最长500字")
        private String summary;

        private String coverImage;

        private Integer status;

        private Set<Long> tagIds;
    }

    @Data
    public static class UpdateRequest {
        private String title;
        private String content;
        private String summary;
        private String coverImage;
        private Integer status;
        private Set<Long> tagIds;
    }

    @Data
    public static class ArticleResponse {
        private Long id;
        private String title;
        private String content;
        private String summary;
        private String coverImage;
        private Integer status;
        private Integer viewCount;
        private String authorName;
        private Long authorId;
        private Set<TagDTO> tags;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    public static class TagDTO {
        private Long id;
        private String name;
    }

    @Data
    public static class ArticlePageRequest {
        private int page = 1;
        private int size = 10;
        private Long tagId;
        private String keyword;
    }
}
