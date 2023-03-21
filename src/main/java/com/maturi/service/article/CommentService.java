package com.maturi.service.article;

import com.maturi.dto.article.ArticleCommentDTO;
import com.maturi.entity.article.Article;
import com.maturi.entity.article.Comment;
import com.maturi.entity.article.CommentStatus;
import com.maturi.entity.article.LikeComment;
import com.maturi.entity.member.Member;
import com.maturi.repository.article.ArticleRepository;
import com.maturi.repository.article.CommentRepository;
import com.maturi.repository.article.LikeCommentRepository;
import com.maturi.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
  final private MemberRepository memberRepository;
  final private ArticleRepository articleRepository;
  final private CommentRepository commentRepository;
  final private LikeCommentRepository likeCommentRepository;
  public Comment write(Long memberId, Long articleId, String commentBody) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
            new IllegalArgumentException("맴버가 없습니다!"));
    Article findArticle =  articleRepository.findById(articleId).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 글입니다!"));

    Comment comment = Comment.builder()
            .member(findMember)
            .article(findArticle)
            .content(commentBody)
            .status(CommentStatus.NORMAL)
            .build();

    Comment findComment = commentRepository.save(comment);
    log.info("writeComment! findComment = {}", findComment);
    return findComment;
  }

  public List<ArticleCommentDTO> newComments(Long memberId, Long articleId, Long commentId) { // 해당 id 이후 댓글 리스트 (id포함)
    List<Comment> newComments = commentRepository.findByArticleIdAndIdGreaterThanAndStatusOrderByIdDesc(articleId, commentId, CommentStatus.NORMAL);
    log.info("newComments = {}", newComments);

    List<ArticleCommentDTO> commentDTOList = new ArrayList<>();

    for(Comment comment : newComments){
      LikeComment likeComment = likeCommentRepository.findByCommentIdAndMemberId(comment.getId(), memberId);

      ArticleCommentDTO commentDTO = ArticleCommentDTO.builder()
              .id(comment.getId())
              .profileImg(comment.getMember().getProfileImg())
              .name(comment.getMember().getName())
              .content(comment.getContent())
              .modifiedDate(comment.getModifiedDate())
              .like(likeCommentRepository.countByCommentId(comment.getId()))
              .isLiked(likeComment != null ? true : false) // 로그인멤버가 좋아요 한상태인지 확인
              .build();
      commentDTOList.add(commentDTO);
    }

    log.info("ArticleCommentDTOList = {}", commentDTOList);
    return commentDTOList;
  }

  public List<ArticleCommentDTO> articleComment(Long memberId, Long articleId) {
    List<Comment> comments = commentRepository.findByArticleIdAndStatusOrderByIdDesc(articleId, CommentStatus.NORMAL);
    log.info("Comments = {}", comments);

    List<ArticleCommentDTO> commentDTOList = new ArrayList<>();

    for(Comment comment : comments){
      LikeComment likeComment = likeCommentRepository.findByCommentIdAndMemberId(comment.getId(), memberId);

      ArticleCommentDTO commentDTO = ArticleCommentDTO.builder()
              .id(comment.getId())
              .profileImg(comment.getMember().getProfileImg())
              .name(comment.getMember().getName())
              .content(comment.getContent())
              .modifiedDate(comment.getModifiedDate())
              .like(likeCommentRepository.countByCommentId(comment.getId()))
              .isLiked(likeComment != null ? true : false) // 로그인멤버가 좋아요 한상태인지 확인
              .build();
      commentDTOList.add(commentDTO);
    }

    log.info("ArticleCommentDTOList = {}", commentDTOList);
    return commentDTOList;
  }

  public boolean likeOrUnlike(Long memberId, Long commentId) {
    LikeComment findLikeComment = likeCommentRepository.findByCommentIdAndMemberId(commentId, memberId);

    Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
            new IllegalArgumentException("해당되는 댓글이 없습니다!"));
    Member findMember = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("로그인 상태가 아닙니다!"));

    if(findLikeComment == null){ // 좋아요 안한 상태
      LikeComment likeComment = LikeComment.builder()
              .comment(findComment)
              .member(findMember)
              .build();
      findLikeComment = likeCommentRepository.save(likeComment);
      return true;
    } else { // 이미 좋아요 한 상태
      likeCommentRepository.delete(findLikeComment);
      return false;
    }
  }

  public int likeNum(Long commentId) {
    return likeCommentRepository.countByCommentId(commentId);
  }


  public String delete(Long memberId, Long commentId) {
    String msg = null;

    Comment findComment = commentRepository.findByIdAndStatus(commentId, CommentStatus.NORMAL);
    if(findComment == null){
      msg = "댓글 삭제 실패! 해당 댓글이 존재하지 않습니다!";
    } else if(!Objects.equals(findComment.getMember().getId(), memberId)){
      msg = "댓글 삭제 실패! 댓글 작성자가 아닙니다!";
      new IllegalArgumentException(msg);
    }
    findComment.changeStatus(CommentStatus.DELETE); // 댓글의 status delete로 수정
    commentRepository.save(findComment); // db update

    return msg; // 정삭 작동 -> null
  }

  public String modify(Long memberId, Long commentId, String commentBody) {
    String msg = null;

    Comment findComment = commentRepository.findByIdAndStatus(commentId, CommentStatus.NORMAL);
    log.info("findComment = {}" + findComment);

    if(findComment == null){
      msg = "댓글 수정 실패! 해당 댓글이 존재하지 않습니다!";
      new IllegalArgumentException(msg);
    } else if(!Objects.equals(findComment.getMember().getId(), memberId)) {
      msg = "댓글 삭제 실패! 댓글 작성자가 아닙니다!";
      new IllegalArgumentException(msg);
    } else {
      findComment.changeContent(commentBody); // 댓글 content 수정
      commentRepository.save(findComment); // db update
    }
    return msg;
  }

  }

