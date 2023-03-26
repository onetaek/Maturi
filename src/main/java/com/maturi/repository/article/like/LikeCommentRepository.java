package com.maturi.repository.article.like;

import com.maturi.entity.article.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository  extends JpaRepository<LikeComment, Long> {
  int countByCommentId(Long commentId);

  LikeComment findByCommentIdAndMemberId(Long commentId, Long memberId);

  void deleteAllByCommentId(Long commentId);
}
