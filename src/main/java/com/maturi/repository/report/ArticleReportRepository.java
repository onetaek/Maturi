package com.maturi.repository.report;

import com.maturi.entity.article.ArticleReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleReportRepository extends JpaRepository<ArticleReport, Long> {
  ArticleReport findByMemberIdAndArticleId(Long memberId, Long articleId);
}
