package com.maturi.controller;

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
    @GetMapping("/popup/dropdown")
    public String popupDropdownTest(){
        return "/popup_dropdown_test";
    }
//    @GetMapping("/popup/show")
//    public String popupShow(){
//        return "/"
//    }
}
