package com.maturi.controller.article;

import com.maturi.dto.article.ArticleDTO;
import com.maturi.dto.article.ArticleViewDTO;
import com.maturi.dto.article.search.ArticleSearchRequest;
import com.maturi.dto.article.RestaurantDTO;
import com.maturi.entity.article.Article;
import com.maturi.service.article.ArticleService;
import com.maturi.service.article.CommentService;
import com.maturi.util.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ArticleController {

    final private ArticleService articleService;
    final private CommentService commentService;

    @GetMapping("/articles")
    public String articlesPage(@Login Long memberId,
                               Model model){
        log.info("findMember = {}",articleService.memberInfo(memberId));
        //게시글의 정보는 Ajax요청으로 데이터를 받아옴
        model.addAttribute("member", articleService.memberInfo(memberId));
        return "/article/welcome";
    }

    @GetMapping("/article")
    public String writeForm(@Login Long memberId,
                            Model model,
                            @ModelAttribute(name = "restaurant") RestaurantDTO restaurantDTO){

        log.info("restaurantDTO={}",restaurantDTO);
        model.addAttribute("member", articleService.memberInfo(memberId));
        return "/article/write";
    }

    @PostMapping("/article")
    public String write(@Login Long memberId,
                        ArticleDTO articleDTO,
                        Model model) throws IOException {
        log.info("/article POST요청");
        log.info("articleDTO={}",articleDTO);

        Long articleId = articleService.write(memberId, articleDTO);
        log.info("articleId={}",articleId);
        model.addAttribute("articleId", articleId);
        return "redirect:/article/"+articleId;
    }

    @GetMapping("/article/{articleId}")
    public String articlePage(@Login Long memberId,
                              @PathVariable Long articleId,
                              Model model){
        // 로그인 멤버 정보
        model.addAttribute("member", articleService.memberInfo(memberId));

        log.info("articleId={}",articleId);

        boolean status = articleService.articleStatusNormal(articleId);
        if(!status){
            model.addAttribute("alertMessage","해당 게시글을 찾을 수 없습니다.");
            return "/layout/message";
        }

        model.addAttribute("article", articleService.articleInfo(articleId));
        model.addAttribute("restaurant", articleService.restaurantByArticle(articleId));
        model.addAttribute("comments", commentService.articleComment(memberId, articleId));
//        model.addAttribute("isLikedComment")
        return "/article/article";
    }

    @DeleteMapping("/article/{articleId}")
    public String delete(@Login Long memberId,
                         @PathVariable Long articleId,
                         Model model){
        log.info("article delete method start!!");

        String msg = articleService.delete(memberId, articleId);

        model.addAttribute("alertMessage", msg);

        return "/layout/message";
    }
}
