package com.maturi.common.test;

import com.maturi.dto.member.AreaInterDTO;
import com.maturi.common.exception.Sample404Exception;
import com.maturi.common.exception.Sample500Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    @RequestMapping
    @GetMapping("/memberNickName")
    public String memberNickName(@RequestParam("memberNickName")String nickName){
        return nickName;
    }
    @GetMapping("")
    public String index(){
        return "article/welcome";
    }
    @GetMapping("/login")
    public String login(){
        return "/member/login";
    }
    @GetMapping("/join")
    public String join(){
        return "members/join";
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

    @GetMapping("/error")
    public String errorText(@ModelAttribute AreaInterDTO areaInterDTO, BindingResult bindingResult,Model model){
        bindingResult.reject("testError","테스트 에러");
        model.addAttribute("test","도레미파");
        return "/error/test";
    }

    @GetMapping("/multipart")
    public String multipart(){
        return "/multipartFileTest";
    }

    @ResponseBody
    @GetMapping("/aws")
    public String awsConTest() {
        return "aws connect successful";
    }

    @GetMapping("/error/400")
    public void error404() {
        throw new Sample404Exception();
    }

    @GetMapping("/error/500")
    public void error500() {
        throw new Sample500Exception();
    }
}
