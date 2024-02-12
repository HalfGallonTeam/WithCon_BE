package com.halfgallon.withcon.domain.tag.service;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import com.halfgallon.withcon.domain.tag.dto.TagSearchDto;
import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import com.halfgallon.withcon.domain.tag.repository.TagRepository;
import com.halfgallon.withcon.domain.tag.repository.TagSearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final TagSearchRepository tagSearchRepository;

  @Override
  @Transactional(readOnly = true)
  public List<TagCountDto> findTagOrderByCount() {
    return tagRepository.findTagOrderByCount();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TagCountDto> findTagNameOrderByCount(String tagName) {
    return tagRepository.findTagNameOrderByCount(tagName);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TagSearchDto> findTagKeyword(String keyword) {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "id"));

    List<TagSearch> tagSearches = tagSearchRepository.findAllByNameStartingWithIgnoreCase(keyword, pageable);
    return tagSearches.stream().map(TagSearchDto::fromEntity).toList();
  }

}
