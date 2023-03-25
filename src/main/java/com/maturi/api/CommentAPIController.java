package com.maturi.api;

import com.maturi.dto.article.ArticleCommentDTO;
import com.maturi.repository.article.CommentRepository;
import com.maturi.service.article.CommentService;
import com.maturi.util.argumentresolver.Login;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentAPIController {
  private final CommentRepository commentRepository;

  private final CommentService commentService;

  @PostMapping("/articles/{article_id}/comment")///api/article/{article_id}/comment 게시글 하나의 작성요청
  public ResponseEntity<List<ArticleCommentDTO>> write(@Login Long memberId,
                                                       @PathVariable Long article_id,
                                                       @RequestBody String json) throws ParseException { // 댓글 작성
    // parse
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
    String commentBody = (String) jsonObject.get("commentBody");

    // 댓글 생성
    commentService.write(memberId, article_id, commentBody);

    // 새 댓글 목록
    List<ArticleCommentDTO> newComments = commentService.articleComment(memberId, article_id);

    return ResponseEntity.status(HttpStatus.OK).body(newComments);
  }

  @DeleteMapping("/comment/{id}")// /api/comment/{id}
  public ResponseEntity<String> delete(@Login Long memberId,
                                       @PathVariable Long id){
    String msg = commentService.delete(memberId, id);
    if(msg != null) {
      log.info("comment delete error message : ", msg);
    }
    return msg == null ?
            ResponseEntity.status(HttpStatus.OK).build() :
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @PatchMapping("/api/comment/{id}")
  public ResponseEntity<String> modify(@Login Long memberId,
                                       @PathVariable Long id,
                                       @RequestBody String json) throws ParseException {
    // parse
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
    String commentBody = (String) jsonObject.get("commentBody");

    // 댓글 수정
    String msg = commentService.modify(memberId, id, commentBody);
    if(msg != null) {
      log.info("comment modify error message : " + msg);
    }

    return msg == null ?
            ResponseEntity.status(HttpStatus.OK).build() :
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @PostMapping("/comment/{id}/like")
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
}
