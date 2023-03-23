package com.maturi.api;

import com.maturi.dto.article.ArticleViewDTO;
import com.maturi.dto.article.search.ArticleSearchRequest;
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
import java.util.List;
import java.util.Map;

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
  public String searchArticlePaging(@Login Long memberId,
                                    @ModelAttribute ArticleSearchRequest articleSearchRequest,
                                    @RequestParam Long lastArticleId,
                                    @PageableDefault(page = 0, size = 5)Pageable pageable){

    log.info("articleSearchRequest={}",articleSearchRequest);
    List<ArticleViewDTO> articleViewDTOS = articleService.articleSearch(articleSearchRequest,memberId,lastArticleId, pageable);


    log.info("[ArticleAPIController] articleViewDTOS={}",articleViewDTOS);

    articleService.articleSearch(articleSearchRequest,memberId,lastArticleId,pageable);

    return null;
  }


}

