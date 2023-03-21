package com.maturi.repository.article;

import com.maturi.entity.article.Comment;
import com.maturi.entity.article.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByArticleIdAndStatusOrderByIdDesc(Long articleId, CommentStatus status);

  List<Comment> findByArticleIdAndIdGreaterThanAndStatusOrderByIdDesc(Long articleId, Long commentId, CommentStatus status);

  Comment findByIdAndStatus(Long commentId, CommentStatus status);
}
