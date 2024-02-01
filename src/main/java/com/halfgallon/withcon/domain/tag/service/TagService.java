package com.halfgallon.withcon.domain.tag.service;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import java.util.List;

public interface TagService {

  List<TagCountDto> findTagOrderByCount();
}
