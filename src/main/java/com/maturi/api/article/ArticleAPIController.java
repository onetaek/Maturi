package com.maturi.api.article;

import com.maturi.dto.article.search.ArticleOrderCond;
import com.maturi.dto.article.search.ArticlePagingRequest;
import com.maturi.dto.article.search.ArticleSearchRequest;
import com.maturi.dto.article.search.ArticlePagingResponse;
import com.maturi.service.article.ArticleService;
import com.maturi.service.article.ReportService;
import com.maturi.service.article.RestaurantService;
import com.maturi.util.argumentresolver.Login;
import com.maturi.util.constfield.OrderConst;
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
  private final ArticleService articleService;
  private final ReportService reportService;


  @PostMapping("/{id}/like")
  public ResponseEntity likeOrUnlike(@Login Long memberId,
                                     @PathVariable Long id) throws ParseException {
    int isLiked = // 좋아요 상태가 됨 -> 1
            articleService.likeOrUnlike(memberId, id)?
            1 : 0;
    int likeNum = articleService.likeNum(id);

    Map<String, Integer> result = new HashMap<>();
    result.put("isLiked", isLiked);
    result.put("likeNum", likeNum);

    return new ResponseEntity(result, HttpStatus.OK);
  }

  @GetMapping("")//게시글 출력 페이징, 검색
  public ResponseEntity<ArticlePagingResponse> searchArticlePaging(@Login Long memberId,
                                                                   @ModelAttribute ArticleSearchRequest articleSearchRequest,//검색조건에 필요한 값들
                                                                   @ModelAttribute ArticlePagingRequest articlePagingRequest,
                                                                   @ModelAttribute ArticleOrderCond articleOrderCond){//페이징에 필요한 값들
    ArticlePagingResponse articles = articleService.articleSearch(articleSearchRequest,articlePagingRequest,articleOrderCond,memberId);
    if(articles == null){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(articles);
  }

  @GetMapping("/members/{id}")
  public ResponseEntity<ArticlePagingResponse> myPageArticlePaging(@ModelAttribute ArticlePagingRequest articlePagingRequest,
                                                                   @PathVariable Long id){ // 마이페이지 회원 ID
    ArticlePagingResponse articles = articleService.articlesByMember(articlePagingRequest, id);
    return ResponseEntity.status(HttpStatus.OK).body(articles);
  }


  @PostMapping("/{id}/report")
  public ResponseEntity reportArticle(@Login Long memberId,
                                      @PathVariable Long id,
                                      @RequestBody Map<String,String> map){

    boolean status = articleService.articleStatusNormal(id); // 게시글 활성화상태인지 체크
    if(!status){ // 게시글 비활성화 상태
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 게시글 신고 (이미 해당 회원이 신고한 내역 있음 -> false)
    boolean isNewReport = reportService.reportArticle(memberId, id,map.get("reportReason"));
    return isNewReport?
            ResponseEntity.status(HttpStatus.OK).build() : // 신고 성공
            ResponseEntity.status(HttpStatus.IM_USED).build(); // 이미 신고한 글
  }

}

