package com.maturi.repository.article.like;

import com.maturi.entity.article.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {
  int countByArticleId(Long articleId);
  LikeArticle findByArticleIdAndMemberId(Long articleId, Long memberId);
}
