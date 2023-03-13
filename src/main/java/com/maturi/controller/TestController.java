package com.maturi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public String index(){
        return "/article/welcome";
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

    @GetMapping("/write/search")
    public String writeMap(){
        return "/article/write_search";
    }

    @GetMapping("/write")
    public String write(){
        return "/article/write";
    }
}
