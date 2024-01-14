package com.maturi.repository.tag;

import com.maturi.entity.tag.TagValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagValueRepository extends JpaRepository<TagValue, Long> {

  List<TagValue> findByArticleId(Long id);

  TagValue findByArticleIdAndTagId(Long articleId, Long id);
}
