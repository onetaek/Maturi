package com.maturi.service.article;

import com.maturi.dto.article.CommentDTO;
import com.maturi.entity.article.Article;
import com.maturi.entity.article.Comment;
import com.maturi.entity.article.CommentStatus;
import com.maturi.entity.article.LikeComment;
import com.maturi.entity.member.Member;
import com.maturi.repository.article.ArticleRepository;
import com.maturi.repository.article.CommentQuerydslRepository;
import com.maturi.repository.article.CommentRepository;
import com.maturi.repository.article.like.LikeCommentRepository;
import com.maturi.repository.member.member.MemberRepository;
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
  private final MemberRepository memberRepository;
  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;
  private final CommentQuerydslRepository commentQRepository;
  private final LikeCommentRepository likeCommentRepository;
  public Comment write(Long memberId,
                       Long articleId,
                       Long ref,
                       Long refStep,
                       Long refMemberId,
                       String refMemberNickName,
                       String content) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
            new IllegalArgumentException("맴버가 없습니다!"));
    Article findArticle =  articleRepository.findById(articleId).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 글입니다!"));

    //새로운 댓글일 경우 ref는 최대 값 + 1, refStep은 1
    //새로운 댓글이 아닐 경우(ref값이 있을 경우) ref그대로 사용, refStep은 refStep + 1
    if(ref == null){
      Long maxRef = commentQRepository.findMaxRef(articleId);
      log.info("maxRef = {}",maxRef);
      ref = maxRef == null ? 1L : maxRef + 1;
    }
    refStep = refStep == null ? 1L : refStep + 1;

    Comment comment = Comment.builder()
            .ref(ref)
            .refStep(refStep)
            .refMemberId(refMemberId)
            .refMemberNickName(refMemberNickName)
            .member(findMember)
            .article(findArticle)
            .content(content)
            .status(CommentStatus.NORMAL)
            .build();

    Comment findComment = commentRepository.save(comment);
    log.info("writeComment! findComment = {}", findComment);
    return findComment;
  }

  public List<CommentDTO> articleComment(Long memberId, Long articleId) {
//    List<Comment> comments = commentQRepository.findByArticleIdAndStatusOrderByIdDesc(articleId);
    List<Comment> comments = commentQRepository.findByArticleId(articleId);
    log.info("Comments = {}", comments);

    List<CommentDTO> commentDTOList = new ArrayList<>();

    for(Comment comment : comments){
      LikeComment likeComment = likeCommentRepository.findByCommentIdAndMemberId(comment.getId(), memberId);

      CommentDTO commentDTO = CommentDTO.builder()
              .id(comment.getId())
              .ref(comment.getRef())
              .refStep(comment.getRefStep())
              .profileImg(comment.getMember().getProfileImg())
              .memberId(comment.getMember().getId())
              .name(comment.getMember().getName())
              .nickName(comment.getMember().getNickName())
              .content(comment.getContent())
              .createdDate(comment.getModifiedDate())
              .likeCount(likeCommentRepository.countByCommentId(comment.getId()))
              .isLiked(likeComment != null ? true : false) // 로그인멤버가 좋아요 한상태인지 확인
              .refMemberId(comment.getRefMemberId())
              .refMemberNickName(comment.getRefMemberNickName())
              .build();
      if(comment.getCreatedDate() != comment.getModifiedDate()){
        commentDTO.setModified(true);
      }else{
        commentDTO.setModified(false);
      }
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

    Comment findComment = commentQRepository.findByIdAndStatus(commentId);
    if(findComment == null){
      msg = "댓글 삭제 실패! 해당 댓글이 존재하지 않습니다!";
    } else if(!Objects.equals(findComment.getMember().getId(), memberId)){
      msg = "댓글 삭제 실패! 댓글 작성자가 아닙니다!";
      new IllegalArgumentException(msg);
    } else { // 댓글 삭제 성공
      findComment.changeStatus(CommentStatus.DELETE); // 댓글의 status delete로 수정
      commentRepository.save(findComment); // db update
    }

    return msg; // 정삭 작동 -> null
  }

  public String modify(Long memberId, Long commentId, String commentBody) {
    String msg = null;

    Comment findComment = commentQRepository.findByIdAndStatus(commentId);
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

  public boolean commentStatusNormal(Long commentId) {
    Comment findComment = commentQRepository.findByIdAndStatus(commentId);

    log.info("findComment by Status = {}", findComment);

    return findComment != null;
  }
}

