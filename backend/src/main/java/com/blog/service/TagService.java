package com.blog.service;

import com.blog.dto.ArticleDTO;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Long createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
        return tag.getId();
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public List<ArticleDTO.TagDTO> getAllTags() {
        return tagRepository.findAll().stream().map(tag -> {
            ArticleDTO.TagDTO dto = new ArticleDTO.TagDTO();
            dto.setId(tag.getId());
            dto.setName(tag.getName());
            return dto;
        }).collect(Collectors.toList());
    }
}
