package com.maturi.controller;

import com.maturi.dto.member.AreaInterDTO;
import com.maturi.dto.member.MemberLoginDTO;
import com.maturi.test.TestArticle;
import com.maturi.util.FileStore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

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
}
