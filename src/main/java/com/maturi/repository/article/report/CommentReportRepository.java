package com.maturi.repository.article.report;

import com.maturi.entity.article.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
  CommentReport findByMemberIdAndCommentId(Long memberId, Long commentId);
}
