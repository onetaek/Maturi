package com.maturi.api;

import com.maturi.service.article.ArticleService;
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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article")
public class ArticleAPIController {
  final private ArticleService articleService;

  @PostMapping("/like/{id}")
  public ResponseEntity<Map<String, Integer>> like(@Login Long memberId,
                                                   @PathVariable Long id) throws ParseException {
    int likeNum = articleService.likeOrUnlike(memberId, id);

    Map<String, Integer> result = new HashMap<>();
    result.put("likeNum", likeNum);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
