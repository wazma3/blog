package com.blog.repository;

import com.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByStatus(Integer status, Pageable pageable);

    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.id = :tagId AND a.status = :status")
    Page<Article> findByTagIdAndStatus(@Param("tagId") Long tagId, @Param("status") Integer status, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.status = :status AND (a.title LIKE %:keyword% OR a.summary LIKE %:keyword%)")
    Page<Article> searchByKeyword(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);

    List<Article> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT a FROM Article a WHERE a.status = 1 ORDER BY a.viewCount DESC")
    List<Article> findTopByViewCount(Pageable pageable);
}
