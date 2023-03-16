package com.maturi.repository;

import com.maturi.entity.article.Article;
import com.maturi.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
