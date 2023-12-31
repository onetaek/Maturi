package com.maturi.service.article;

import com.maturi.entity.article.*;
import com.maturi.entity.member.Member;
import com.maturi.repository.article.ArticleQuerydslRepository;
import com.maturi.repository.article.ArticleRepository;
import com.maturi.repository.article.CommentQuerydslRepository;
import com.maturi.repository.article.CommentRepository;
import com.maturi.repository.article.report.ArticleReportRepository;
import com.maturi.repository.article.report.CommentReportRepository;
import com.maturi.repository.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReportService {
  private final CommentReportRepository commentReportRepository;
  final private ArticleReportRepository articleReportRepository;
  final private ArticleRepository articleRepository;
  final private CommentRepository commentRepository;
  final private MemberRepository memberRepository;
  final private ArticleQuerydslRepository articleQRepository;
  final private CommentQuerydslRepository commentQRepository;

  /**
   * 게시글 신고하기
   * @param memberId
   * @param articleId
   * @return boolean (새로운 신고자&&신고글 여부)
   */
  public boolean reportArticle(Long memberId, Long articleId,String reportReason) {
    Member member = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));
    Article findArticle = articleQRepository.findByIdAndStatus(articleId);

    // 이미 신고 여부.. 확인
    ArticleReport findArticleReport = articleReportRepository.findByMemberIdAndArticleId(memberId, articleId);
    if(findArticleReport != null){ // 이미 해당 회원이 신고한 글이면 ...
      return false;
    }

    // articleReport insert
    ArticleReport articleReport = ArticleReport.builder()
            .member(member)
            .article(findArticle)
            .reportReason(reportReason)
            .status(ReportStatus.WAITING) // 처리 대기 상태 ..
            .build();
    articleReportRepository.save(articleReport); // db에 저장

    // article의 status 업데이트
    findArticle.changeStatus(ArticleStatus.REPORT);
    articleRepository.save(findArticle);

    return true;
  }

  /**
   * 댓글 신고하기
   * @param memberId
   * @param commentId
   * @return boolean (새로운 신고자&&신고댓글 여부)
   */
  public boolean reportComment(Long memberId, Long commentId,String reportReason) {
    Member member = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));
    Comment findComment = commentQRepository.findByIdAndStatus(commentId);

    // 이미 신고 여부.. 확인
    CommentReport findCommentReport = commentReportRepository.findByMemberIdAndCommentId(memberId, commentId);
    if(findCommentReport != null){ // 이미 해당 회원이 신고한 댓글이면 ...
      return false;
    }

    // commentReport insert
    CommentReport commentReport = CommentReport.builder()
            .member(member)
            .comment(findComment)
            .reportReason(reportReason)
            .status(ReportStatus.WAITING) // 처리 대기 상태 ..
            .build();
    commentReportRepository.save(commentReport); // db에 저장

    // comment의 status 업데이트
    findComment.changeStatus(CommentStatus.REPORT);
    commentRepository.save(findComment);

    return true;
  }
}
