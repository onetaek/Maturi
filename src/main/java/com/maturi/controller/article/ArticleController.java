package com.maturi.controller.article;

import com.maturi.dto.member.MemberDTO;
import com.maturi.entity.member.Member;
import com.maturi.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ArticleController {

    final private ArticleService articleService;

    @GetMapping("/articles")
    public String articlesPage(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");

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
