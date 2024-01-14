package com.maturi.repository.comment;

import com.maturi.entity.comment.Comment;
import com.maturi.entity.comment.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  // 조건 : articleId, Status=normal / id의 역순
  List<Comment> findByArticleIdAndStatusOrderByIdDesc(Long articleId, CommentStatus status);

  List<Comment> findByArticleIdAndIdGreaterThanAndStatusOrderByIdDesc(Long articleId, Long commentId, CommentStatus status);

  Comment findByIdAndStatus(Long commentId, CommentStatus status);
  int countByArticleId(Long articleId);
}
