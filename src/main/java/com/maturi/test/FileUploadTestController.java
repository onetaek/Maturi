package com.maturi.test;

import com.maturi.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class FileUploadTestController {

    private final MemoryArticleRepository memoryArticleRepository;
    private final FileStore fileStore;
    @Value("${file.dir}")
    private String fileDir;

    //이건 그냥 업로드하는 최소한의 코드만 따로 빼둔거 참고만
    @PostMapping("/sample/upload")
    public String simpleUpload(@RequestParam MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            file.transferTo(new File(fullPath));
        }
        return null;
    }

    //테스트 파일 업로드 페이지로 이동
    @GetMapping("/file/upload")
    public String fileUploadPage(){
        return "/file_test";
    }

    //파일 저장 로직 이부분이 DB에 저장안하고 메모리에 저장했다는 로직이있는 메서드
    @PostMapping("/file/upload")
    public String fileUpload(@ModelAttribute TestArticleDTO testArticleDTO,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) throws IOException {
        //application.properties에서 지정한 file.dir 경로에 파일이 저장됨
        List<String> storeImageFiles = fileStore.storeFiles(testArticleDTO.getImageFiles(), request);

        //메모리에 저장(실제로 구현할때는 이 로직을 파일명 문자열로 바꾸고나서 Entity에 맞게 바꿔주고 여차저차 해야함
        TestArticle testArticle = new TestArticle();
        testArticle.setTitle(testArticleDTO.getTitle());
        testArticle.setContent(testArticleDTO.getContent());
        testArticle.setImageFiles(storeImageFiles);
        memoryArticleRepository.save(testArticle);

        redirectAttributes.addAttribute("articleId",testArticle.getId());

        return "redirect:/test/file/show/{articleId}";
    }

    //파일 보기위한 페이지 이동
    @GetMapping("/file/show/{id}")
    public String filesViewPage(@PathVariable Long id, Model model) {

        TestArticle testArticle = memoryArticleRepository.findById(id);//메모리에 저장한 게시글 정보(랜덤 파일명, 기존 파일명 포함)을 꺼내옴
        model.addAttribute("testArticle",testArticle);//프로젝트에서는 DTO로 변환해서 담아주기

        return "/file_show_test";
    }

    //이미지를 출력하기 위한 요청
//    @ResponseBody
//    @GetMapping("/file/{filename}")
//    public Resource showImg(@PathVariable String filename) throws MalformedURLException {
//        String fullPath = fileStore.getFullPath(filename);
//        UrlResource urlResource = new UrlResource("file:" + fullPath);
//        return urlResource;
//    }

}
