package com.maturi.repository.article;

import com.maturi.entity.article.Article;
import com.maturi.entity.article.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> ,ArticleRepositoryCustom{
  Article findByIdAndStatus(Long articleId, ArticleStatus status);
}
