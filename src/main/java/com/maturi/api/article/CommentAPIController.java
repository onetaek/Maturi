package com.maturi.api.article;

import com.maturi.dto.article.CommentDTO;
import com.maturi.dto.article.CommentRequest;
import com.maturi.service.article.CommentService;
import com.maturi.service.article.ReportService;
import com.maturi.util.argumentresolver.Login;
import com.maturi.util.constfield.MessageConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.maturi.util.constfield.MessageConst.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentAPIController {
  private final CommentService commentService;
  final private ReportService reportService;

  @GetMapping("/articles/{articleId}/comments")
  public ResponseEntity<List<List<CommentDTO>>> getComments(@Login Long memberId,
                                                      @PathVariable Long articleId){
    List<List<CommentDTO>> comments = commentService.articleComment(memberId, articleId);

    return ResponseEntity.status(HttpStatus.OK).body(comments);
  }

  @PostMapping("/articles/{articleId}/comments")///api/article/{article_id}/comment 게시글 하나의 작성요청
  public ResponseEntity write(@Login Long memberId,
                              @PathVariable Long articleId,
                              @RequestBody CommentRequest commentRequest) throws ParseException { // 댓글 작성
    //댓글 작성시 HTTP message body로 받아야할 값
    //1. ref값 -> null일 경우 1로 저장
    //2. refStep -> null일 경우 1로 저장
    log.info("commentRequest = {}",commentRequest);
    // 댓글 생성
    commentService.write(memberId,
            articleId,
            commentRequest.getRef(),
            commentRequest.getRefStep(),
            commentRequest.getRefMemberId(),
            commentRequest.getRefMemberNickName(),
            commentRequest.getContent());
    //GET메서드를 만들어서 그걸로 가져오는 걸로 변경
    // 새 댓글 목록
//    List<ArticleCommentDTO> newComments = commentService.articleComment(memberId, article_id);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  //댓글 삭제
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity remove(@Login Long memberId,
                               @PathVariable Long commentId,
                               @RequestBody Map<String,Long> map){
    log.info("delete comment 시작!");
    log.info("commentId = {}",commentId);
    log.info("articleId = {}",map.get("articleId"));
    log.info("memberId = {}",memberId);
    log.info("ref = {}",map.get("ref"));
    switch (commentService.remove(commentId,map.get("articleId"), memberId, map.get("ref"))){
      case NOT_FOUND:
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//404
      case NOT_WRITER:
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();//403
      case UPDATE_FAIL:
        return ResponseEntity.status(HttpStatus.CONFLICT).build();//409
      case DELETE_SUCCESS:
        return ResponseEntity.status(HttpStatus.OK).build();
      default:
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
  @PatchMapping("/comments/{commentId}")
  public ResponseEntity update(@Login Long memberId,
                               @PathVariable Long commentId,
                               @RequestBody Map<String,String> map){
    log.info("commentId",commentId);
    log.info("content",map.get("content"));
    String status = commentService.update(memberId, commentId, map.get("content"));
    switch (status){
      case NOT_FOUND:
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//404
      case NOT_WRITER:
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();//403
      case SUCCESS_MESSAGE:
        return ResponseEntity.status(HttpStatus.OK).build();
      default:
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  //댓글 좋아요 클릭시 좋아요 갯수 증가/감소
  @PostMapping("/comments/{id}/like")
  public ResponseEntity<Map<String, Integer>> likeOrUnlike(@Login Long memberId,
                                                           @PathVariable Long id){
    int isLiked = // 좋아요 상태가 됨 -> 1
            commentService.likeOrUnlike(memberId, id) ?
                    1 : 0;
    int likeNum = commentService.likeNum(id);

    Map<String, Integer> result = new HashMap<>();
    result.put("isLiked", isLiked);
    result.put("likeNum", likeNum);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  //댓글 신고기능
  @PostMapping("/comments/{id}/report")
  public ResponseEntity reportComment(@Login Long memberId,
                                      @PathVariable Long id,
                                      @RequestBody Map<String,String> map){

    boolean status = commentService.commentStatusNormal(id); // 댓글 활성화상태인지 체크
    if(!status){ // 댓글 비활성화 상태
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 댓글 신고 (이미 해당 회원이 신고한 내역 있음 -> false)
    boolean isNewReport = reportService.reportComment(memberId, id,map.get("reportReason"));
    log.info("isNewReport?? " + isNewReport);
    return isNewReport?
            ResponseEntity.status(HttpStatus.OK).build() : // 신고 성공
            ResponseEntity.status(HttpStatus.IM_USED).build(); // 이미 신고한 글
  }
}
