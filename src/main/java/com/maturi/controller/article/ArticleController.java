package com.maturi.controller.article;

import com.maturi.service.article.ArticleService;
import com.maturi.util.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ArticleController {

    final private ArticleService articleService;

    @GetMapping("/articles")
    public String articlesPage(@SessionAttribute(name = SessionConst.MEMBER_ID) Long memberId,
                               Model model){

        log.info("findMember = {}",articleService.memberInfo(memberId));
        model.addAttribute("member", articleService.memberInfo(memberId));
        return "/article/welcome";
    }

    @GetMapping("/article/write")
    public String write(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");

        model.addAttribute("member", articleService.memberInfo(memberId));
        return "/article/write";
    }

}
