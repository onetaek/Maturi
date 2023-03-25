package com.maturi.api;

import com.maturi.dto.article.search.ArticlePagingRequest;
import com.maturi.dto.article.search.ArticleSearchRequest;
import com.maturi.dto.article.search.ArticlePagingResponse;
import com.maturi.service.article.ArticleService;
import com.maturi.service.article.RestaurantService;
import com.maturi.util.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleAPIController {
  final private ArticleService articleService;


  @PostMapping("/{id}/like")
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

  @GetMapping("")//게시글 출력 페이징, 검색
  public ResponseEntity<ArticlePagingResponse> searchArticlePaging(@Login Long memberId,
                                                                   @ModelAttribute ArticleSearchRequest articleSearchRequest,//검색조건에 필요한 값들
                                                                   @ModelAttribute ArticlePagingRequest articlePagingRequest){//페이징에 필요한 값들
    log.info("articleSearchRequest = {}",articleSearchRequest);
    log.info("articlePagingRequest = {}",articlePagingRequest);
    ArticlePagingResponse articles = articleService.articleSearch(articleSearchRequest,articlePagingRequest,memberId);

    return ResponseEntity.status(HttpStatus.OK).body(articles);
  }




}

