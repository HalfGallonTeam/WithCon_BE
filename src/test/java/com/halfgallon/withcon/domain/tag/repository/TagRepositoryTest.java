package com.halfgallon.withcon.domain.tag.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.halfgallon.withcon.domain.tag.dto.TagCountDto;
import com.halfgallon.withcon.domain.tag.entity.Tag;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
class TagRepositoryTest {

  @Autowired
  private TagRepository tagRepository;

  @Test
  @DisplayName("태그 정보 조회 - 태그 갯수 많은 순으로 정렬")
  void findTagOrderByCount() {
    //given
    for (int i = 0; i < 5; i++) {
      tagRepository.save(Tag.builder()
          .name("#1번채팅방")
          .build());
    }

    for (int i = 0; i < 3; i++) {
      tagRepository.save(Tag.builder()
          .name("#2번채팅방")
          .build());
    }

    for (int i = 0; i < 2; i++) {
      tagRepository.save(Tag.builder()
          .name("#10번채팅방")
          .build());
    }

    //when
    List<TagCountDto> tagOrderByCount = tagRepository.findTagOrderByCount();

    //then
    assertThat(tagOrderByCount.get(0).getCount()).isEqualTo(5);
    assertThat(tagOrderByCount.get(0).getName()).isEqualTo("#1번채팅방");
  }
}