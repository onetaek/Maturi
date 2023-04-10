package com.maturi.repository.article;

import com.maturi.entity.article.Comment;
import com.maturi.entity.article.CommentStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.member.QMember.member;
import static com.maturi.entity.article.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CommentQuerydslRepository {

  //querydsl을 쓰기위한 객체
  private final JPAQueryFactory query;

  // WHERE article_id = ? AND (status = NORMAL OR status = REPORT) : 검색 가능한 댓글인지 확인 및 객체
  public Comment findByIdAndStatus(Long commentId){
    BooleanBuilder builder = new BooleanBuilder();
    builder.or(comment.status.eq(CommentStatus.NORMAL))
            .or(comment.status.eq(CommentStatus.REPORT));
    return query.selectFrom(comment)
            .join(comment.member, member)
            .join(comment.article, article)
            .fetchJoin()
            .where(
                    commentIdEq(commentId),
                    builder
            )
            .fetchOne();//이거쓰면 하나만 가져올 수 있어요!
  }

  public List<Comment> findByArticleId(Long articleId){
    BooleanBuilder builder = new BooleanBuilder();
    builder.or(comment.status.eq(CommentStatus.NORMAL))
            .or(comment.status.eq(CommentStatus.REPORT));
    return query.selectFrom(comment)
            .join(comment.member, member)
            .join(comment.article, article)
            .fetchJoin()
            .where(
                    articleIdEq(articleId),
                    builder
            )
            .orderBy(
                    comment.ref.asc(),
                    comment.createdDate.asc()
            )
            .fetch();
  }


  public List<Comment> findByArticleIdAndStatusOrderByIdDesc(Long articleId){
    BooleanBuilder builder = new BooleanBuilder();
    builder.or(comment.status.eq(CommentStatus.NORMAL))
            .or(comment.status.eq(CommentStatus.REPORT));
    return query.selectFrom(comment)
            .join(comment.member, member)
            .join(comment.article, article)
            .fetchJoin()
            .where(
                    articleIdEq(articleId),
                    builder
            )
            .orderBy(comment.id.desc())
            .fetch();
  }
  public Long findMaxRef(Long articleId) {
    return query.select(comment.ref)
            .from(comment)
            .where(articleIdEq(articleId))
            .orderBy(comment.ref.desc())
            .fetchFirst();
  }

  private BooleanExpression commentIdEq(Long commentId) {
    return commentId != null ? comment.id.eq(commentId) : null;
  }
  private BooleanExpression articleIdEq(Long articleId) {
    return articleId != null ? article.id.eq(articleId) : null;
  }

}
