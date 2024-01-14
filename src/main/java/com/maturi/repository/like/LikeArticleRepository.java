package com.maturi.repository.like;

import com.maturi.entity.like.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {
  int countByArticleId(Long articleId);
  LikeArticle findByArticleIdAndMemberId(Long articleId, Long memberId);
}
