package com.maturi.repository.report;

import com.maturi.entity.comment.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
  CommentReport findByMemberIdAndCommentId(Long memberId, Long commentId);
}
