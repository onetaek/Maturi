package com.maturi.repository.article;

import com.maturi.entity.article.Article;
import com.maturi.entity.article.TagValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface TagValueRepository extends JpaRepository<TagValue, Long> {

  List<TagValue> findByArticleId(Long id);
}
