package com.maturi.controller;

import com.maturi.dto.member.MemberLoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public String index(){
        return "article/welcome";
    }
    @GetMapping("/login")
    public String login(){
        return "/member/login";
    }
    @GetMapping("/myPage")
    public String myPage(){
        return "/member/myPage";
    }
    @GetMapping("/join")
    public String join(){
        return "/member/join";
    }
    @GetMapping("/article")
    public String article(){
        return "/article/article";
    }
    @GetMapping("/follow")
    public String follow(){
        return "/member/follow";
    }

    @GetMapping("/write")
    public String write(){
        return "/article/write";
    }


    @GetMapping("/kakao/login")
    public String kakaoLogin(RedirectAttributes redirectAttributes){
        log.info("/test/kakao/login, memberLoginDTO전송");
        String email = "84381718@k.com";
        String passwd = "test1234!";

        MemberLoginDTO memberLoginDTO = MemberLoginDTO
                .builder()
                .email(email)
                .passwd(passwd)
                .build();
//        redirectAttributes.addAttribute("memberLoginDTO",memberLoginDTO);
        redirectAttributes.addAttribute("email",email);
        redirectAttributes.addAttribute("passwd",passwd);
        return "redirect:/test/kakao/loginProcess";
    }

    @GetMapping("/kakao/loginProcess")
    public String kakaoLoginProcess(@ModelAttribute MemberLoginDTO memberLoginDTO){

        log.info("/test/kakao/login, memberLoginDTO받음");
        log.info("redirectAttributes로 받은 memberLoginDTO = {}",memberLoginDTO);

        return null;
    }

}
