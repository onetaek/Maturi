package com.maturi.repository.article;

import com.maturi.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> ,ArticleRepositoryCustom{
}
