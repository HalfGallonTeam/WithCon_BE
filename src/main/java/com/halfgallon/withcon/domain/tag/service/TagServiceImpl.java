package com.halfgallon.withcon.domain.tag.service;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import com.halfgallon.withcon.domain.tag.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

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

}
