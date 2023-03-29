package com.maturi.controller.article;

import com.maturi.dto.article.*;
import com.maturi.service.article.ArticleService;
import com.maturi.service.article.CommentService;
import com.maturi.util.argumentresolver.Login;
import com.maturi.util.constfield.MessageConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    final private ArticleService articleService;
    final private CommentService commentService;

    @GetMapping("")//게시글 전체 페이지 이동
    public String articlesPage(@Login Long memberId,
                               Model model){
        log.info("findMember = {}",articleService.memberInfo(memberId));
        //게시글의 정보는 Ajax요청으로 데이터를 받아옴
        model.addAttribute("member", articleService.memberInfo(memberId));
        return "/articles/welcome";
    }

    @GetMapping("/new")//게시글 작성 화면 이동
    public String writeForm(@Login Long memberId,
                            Model model,
                            @ModelAttribute(name = "restaurant") RestaurantDTO restaurantDTO){

        log.info("restaurantDTO={}",restaurantDTO);
        model.addAttribute("member", articleService.memberInfo(memberId));
        return "/articles/write";
    }

    @PostMapping("/new")//게시글 작성 요청
    public String write(@Login Long memberId,
                        @ModelAttribute ArticleDTO articleDTO,
                        Model model) throws IOException {
        log.info(" POST요청");
        log.info("articleDTO={}",articleDTO);

        Long articleId = articleService.write(memberId, articleDTO);
        log.info("articleId={}",articleId);
        model.addAttribute("articleId", articleId);
        return "redirect:/articles/"+articleId;
    }

    @GetMapping("/{articleId}")//게시글 상세 페이지 이동
    public String articlePage(@Login Long memberId,
                              @PathVariable Long articleId,
                              Model model){
        log.info("게시글 상세 페이지 이동");
        // 로그인 멤버 정보
        model.addAttribute("member", articleService.memberInfo(memberId));

        log.info("articleId={}",articleId);

        boolean status = articleService.articleStatusNormal(articleId);
        if(!status){
            log.info("게시글이 없습니다!");
            model.addAttribute("alertMessage","해당 게시글을 찾을 수 없습니다.");
            return "/layouts/message";
        }

        model.addAttribute("article", articleService.articleInfo(articleId,memberId));
        model.addAttribute("restaurant", articleService.restaurantByArticle(articleId));
        model.addAttribute("comments", commentService.articleComment(memberId, articleId));
//        model.addAttribute("isLikedComment")
        return "/articles/article";
    }

    @DeleteMapping("/{articleId}")//게시글 삭제요청
    public String delete(@Login Long memberId,
                         @PathVariable Long articleId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes,
                         Model model){

        String msg = articleService.delete(memberId, articleId);
//        model.addAttribute("alertMessage", msg);
        if(msg.equals(MessageConst.SUCCESS_MESSAGE)){
            redirectAttributes.addFlashAttribute(MessageConst.SUCCESS_MESSAGE, MessageConst.DELETE_SUCCESS);
        } else if(msg.equals(MessageConst.NOT_FOUND)) { // 게시물 찾을 수 없음
            redirectAttributes.addFlashAttribute(MessageConst.ERROR_MESSAGE, MessageConst.NOT_FOUND);
        } else { // 게시물 삭제 권한 없음 (글 작성자가 아님)
            redirectAttributes.addFlashAttribute(MessageConst.ERROR_MESSAGE, MessageConst.NO_PERMISSION);
        }

        String referer = request.getHeader("Referer");
        if(referer.contains("/articles/")){
            return "redirect:/articles";
        }
        return "redirect:" + referer;
    }

    @GetMapping("/{articleId}/edit") // 게시글 수정페이지 이동
    public String editArticlePage(@Login Long memberId,
                           @PathVariable Long articleId,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes,
                           Model model){

        ArticleEditViewDTO articleEditViewDTO = articleService.articleEditInfo(articleId);
        if(!memberId.equals(articleEditViewDTO.getMemberId())){
            String referer = request.getHeader("Referer");
            log.info("referer : " + referer);
            redirectAttributes.addFlashAttribute(MessageConst.ERROR_MESSAGE, MessageConst.NO_PERMISSION);
            return "redirect:" + referer;
        }
        model.addAttribute("member", articleService.memberInfo(memberId));
        model.addAttribute("article", articleEditViewDTO);
        return "/articles/edit";
    }

    @PostMapping("/{articleId}/edit") // 게시글 수정 요청
    public String editArticle(@Login Long memberId,
                              @PathVariable Long articleId,
                              ArticleEditDTO articleEditDTO,
                              Model model) throws IOException {

        log.info("articleEditDTO = {}", articleEditDTO);

        ArticleViewDTO articleViewDTO = articleService.edit(memberId, articleId, articleEditDTO);

        return "redirect:/articles/"+articleId;
    }
}
