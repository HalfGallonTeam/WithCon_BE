package com.halfgallon.withcon.domain.tag.entity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Builder
@Document(indexName = "tag")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Mapping(mappingPath = "static/elastic/tag-mappings.json")
@Setting(settingPath = "static/elastic/elastic-settings.json")
public class TagSearch {
  @Id
  private String id;

  @Field(type = FieldType.Text, name = "name")
  private String name;

  @Field(type = FieldType.Integer, name = "tag_count")
  private Integer tagCount;

  @Field(type = FieldType.Text, name = "performance_id")
  private String performanceId;
}
