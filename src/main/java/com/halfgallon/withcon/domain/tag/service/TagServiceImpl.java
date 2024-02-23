package com.halfgallon.withcon.domain.tag.service;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import com.halfgallon.withcon.domain.tag.dto.TagSearchDto;
import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import com.halfgallon.withcon.domain.tag.repository.TagRepository;
import com.halfgallon.withcon.domain.tag.repository.TagSearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
  public List<TagSearchDto> findTagKeyword(String performanceId, String keyword) {
    List<TagSearch> searches = tagSearchRepository.findTagAutoComplete(performanceId, keyword);
    return searches.stream().map(TagSearchDto::fromEntity).toList();
  }

}
