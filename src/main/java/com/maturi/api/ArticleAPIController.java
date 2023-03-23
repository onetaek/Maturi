package com.maturi.api;

import com.maturi.dto.article.search.ArticleSearchRequest;
import com.maturi.dto.article.search.ArticlePaging;
import com.maturi.service.article.ArticleService;
import com.maturi.util.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.maturi.util.constfield.PagingConst.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArticleAPIController {
  final private ArticleService articleService;

  @PostMapping("/article/likeOrUnlike/{id}")
  public ResponseEntity likeOrUnlike(@Login Long memberId,
                                     @PathVariable Long id) throws ParseException {
//    ResponseEntity
//    int likeNum = articleService.likeOrUnlike(memberId, id);

    int isLiked = // 좋아요 상태가 됨 -> 1
            articleService.likeOrUnlike(memberId, id)?
            1 : 0;
//    boolean isLiked = articleService.likeOrUnlike(memberId, id);
    int likeNum = articleService.likeNum(id);

    Map<String, Integer> result = new HashMap<>();
    result.put("isLiked", isLiked);
    result.put("likeNum", likeNum);

    return new ResponseEntity(result, HttpStatus.OK);
  }

  @GetMapping("/articles")
  public ResponseEntity<ArticlePaging> searchArticlePaging(@Login Long memberId,
                                                           @ModelAttribute ArticleSearchRequest articleSearchRequest,
                                                           @RequestParam Long lastArticleId,
                                                           @PageableDefault(page = 0, size = size)Pageable pageable){

    log.info("articleSearchRequest={}",articleSearchRequest);
    log.info("lastArticleId={}",lastArticleId);
    log.info("pageable={}",pageable);
    ArticlePaging articles = articleService.articleSearch(articleSearchRequest,memberId,lastArticleId, pageable);
    log.info("[ArticleAPIController] articleViewDTOS={}",articles);
    return ResponseEntity.status(HttpStatus.OK).body(articles);
  }


}

