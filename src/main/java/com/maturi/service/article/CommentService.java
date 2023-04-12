package com.maturi.service.article;

import com.maturi.dto.article.CommentDTO;
import com.maturi.entity.BaseTimeEntity;
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
import com.maturi.util.constfield.MessageConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.maturi.util.constfield.MessageConst.*;

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
    Article findArticle = articleRepository.findById(articleId).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 글입니다!"));

    //새로운 댓글일 경우 ref는 최대 값 + 1, refStep은 1
    //새로운 댓글이 아닐 경우(ref값이 있을 경우) ref그대로 사용, refStep은 refStep + 1
    if (ref == null) {
      Long maxRef = commentQRepository.findMaxRef(articleId);
      log.info("maxRef = {}", maxRef);
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
    log.info("write findComment = {}",findComment);
    return findComment;
  }

  public List<List<CommentDTO>> articleComment(Long memberId, Long articleId) {
    List<Comment> comments = commentQRepository.findByArticleId(articleId);
    List<CommentDTO> commentDTOs = createCommentDTO(memberId, comments);
    List<List<CommentDTO>> groupComments = groupComments(commentDTOs);

    groupComments.forEach(groupComment -> {
      log.info("{}번째 댓글 묶음 -> 길이 : {}", groupComments.indexOf(groupComment), groupComment.size());
      groupComment.forEach(commentDTO -> log.info("commentDTO = {}", commentDTO));
    });

    return groupComments;
  }

  public boolean likeOrUnlike(Long memberId, Long commentId) {
    LikeComment findLikeComment = likeCommentRepository.findByCommentIdAndMemberId(commentId, memberId);

    Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
            new IllegalArgumentException("해당되는 댓글이 없습니다!"));
    Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
            new IllegalArgumentException("로그인 상태가 아닙니다!"));

    if (findLikeComment == null) { // 좋아요 안한 상태
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
    if (findComment == null) {
      msg = "댓글 삭제 실패! 해당 댓글이 존재하지 않습니다!";
    } else if (!Objects.equals(findComment.getMember().getId(), memberId)) {
      msg = "댓글 삭제 실패! 댓글 작성자가 아닙니다!";
      new IllegalArgumentException(msg);
    } else { // 댓글 삭제 성공
      findComment.changeStatus(CommentStatus.DELETE); // 댓글의 status delete로 수정
      commentRepository.save(findComment); // db update
    }

    return msg; // 정삭 작동 -> null
  }

  public String remove(Long commentId,Long articleId, Long memberId, Long ref){
    Comment findComment = commentQRepository.findByIdAndStatus(commentId);
    if(findComment == null){
      return NOT_FOUND;
    }else if(!Objects.equals(findComment.getMember().getId(), memberId)){
      return NOT_WRITER;
    }else{
     if(findComment.getRefStep().equals(1L)){//최상위 계층의 댓글일 경우 하위에 있는 댓글 모두 삭제한다.
       boolean isSuccess = commentQRepository.updateStatusToDelete(articleId, ref);
       if(!isSuccess){
         return UPDATE_FAIL;
       }
     }else{
       findComment.changeStatus(CommentStatus.DELETE);
     }
    }
    return DELETE_SUCCESS;
  }


  public String modify(Long memberId, Long commentId, String content) {
    String msg = null;

    Comment findComment = commentQRepository.findByIdAndStatus(commentId);
    log.info("findComment = {}" + findComment);

    if (findComment == null) {
      msg = "댓글 수정 실패! 해당 댓글이 존재하지 않습니다!";
      new IllegalArgumentException(msg);
    } else if (!Objects.equals(findComment.getMember().getId(), memberId)) {
      msg = "댓글 삭제 실패! 댓글 작성자가 아닙니다!";
      new IllegalArgumentException(msg);
    } else {
      findComment.changeContent(content); // 댓글 content 수정
      commentRepository.save(findComment); // db update
    }
    return msg;
  }

  public boolean commentStatusNormal(Long commentId) {
    Comment findComment = commentQRepository.findByIdAndStatus(commentId);

    log.info("findComment by Status = {}", findComment);

    return findComment != null;
  }

  private List<CommentDTO> createCommentDTO(Long memberId, List<Comment> comments) {
    return comments.stream().map(comment -> {
              LikeComment likeComment = likeCommentRepository.findByCommentIdAndMemberId(comment.getId(), memberId);
              boolean isLiked = likeComment != null;
              log.info("comment의 createdDate = {}",comment.getCreatedDate());
              log.info("comment의 modifiedDate = {}",comment.getModifiedDate());
              boolean isModified = !comment.getCreatedDate().equals(comment.getModifiedDate());
              log.info("두 날짜의 값이 다른가요? = {}",isModified);
              return CommentDTO.builder()
                      .id(comment.getId())
                      .ref(comment.getRef())
                      .refStep(comment.getRefStep())
                      .profileImg(comment.getMember().getProfileImg())
                      .memberId(comment.getMember().getId())
                      .name(comment.getMember().getName())
                      .nickName(comment.getMember().getNickName())
                      .content(comment.getContent())
                      .duration(BaseTimeEntity.getDurationByDate(comment.getCreatedDate()))
                      .likeCount(likeCommentRepository.countByCommentId(comment.getId()))
                      .isLiked(isLiked)
                      .refMemberId(comment.getRefMemberId())
                      .refMemberNickName(comment.getRefMemberNickName())
                      .isModified(isModified)
                      .build();
            }).peek(commentDTO -> log.info("ArticleCommentDTOList = {}", commentDTO))
            .collect(Collectors.toList());
  }

  private List<List<CommentDTO>> groupComments(List<CommentDTO> comments) {

    List<List<CommentDTO>> groupedComments = new ArrayList<>();
    List<CommentDTO> currentGroup = null;
    Long previousRef = null;

    // 1. 현재 댓글과 이전 댓글의 ref 값을 비교하여 같은 그룹에 속하는지를 판단합니다.
    for (CommentDTO comment : comments) {
      Long currentRef = comment.getRef();

      // 2. 같은 그룹에 속하지 않는다면 새로운 그룹을 만들어서 DTO를 추가합니다.
      if (currentGroup == null || !previousRef.equals(currentRef)) {
        currentGroup = new ArrayList<>();
        groupedComments.add(currentGroup);
      }

      // 3. 같은 그룹에 속한다면, 현재 그룹의 DTO에 댓글을 추가합니다.
      currentGroup.add(comment);
      previousRef = currentRef;
    }
    // 4. 그룹화된 DTO 리스트를 반환합니다.
    return groupedComments;
  }
}
