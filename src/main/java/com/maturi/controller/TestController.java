package com.maturi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public String index(){
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/myPage")
    public String myPage(){
        return "myPage";
    }
    @GetMapping("/join")
    public String join(){
        return "join";
    }
    @GetMapping("/article")
    public String article(){
        return "article";
    }
}
