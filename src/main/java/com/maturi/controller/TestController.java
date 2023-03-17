package com.maturi.controller;

import com.maturi.dto.member.MemberLoginDTO;
import com.maturi.entity.article.UploadFile;
import com.maturi.util.FileStore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final FileStore fileStore;

    @Value("${file.dir}")
    private String fileDir;

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

    /**
     *
     */
    @PostMapping("/sample/upload")
    public String simpleUpload(@RequestParam MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            file.transferTo(new File(fullPath));
        }

        return null;
    }


    @GetMapping("/file/upload")
    public String fileUploadPage(){
        return "/file_test";
    }
    @ResponseBody
    @PostMapping("/file/upload")
    public List<UploadFile> fileUpload(@ModelAttribute TestArticle testArticle) throws IOException {

        //application.properties에서 지정한 file.dir 경로에 파일이 저장됨
        List<UploadFile> uploadFiles = fileStore.storeFiles(testArticle.getImageFiles());

        //파일명 처리작업

        //DB에 저장하는 작업

        log.info("uploadFiles={}",uploadFiles);
        return uploadFiles;
    }

    @Data
    @ToString
    static public class TestArticle{
        private String title;
        private String content;
        private List<MultipartFile> imageFiles;
    }




}
