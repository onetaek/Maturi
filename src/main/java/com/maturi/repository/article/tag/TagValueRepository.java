package com.maturi.repository.article.tag;

import com.maturi.entity.article.Article;
import com.maturi.entity.article.Tag;
import com.maturi.entity.article.TagValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface TagValueRepository extends JpaRepository<TagValue, Long> {

  List<TagValue> findByArticleId(Long id);

  TagValue findByArticleIdAndTagId(Long articleId, Long id);
}
