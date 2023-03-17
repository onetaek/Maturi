package com.maturi.test;

import com.maturi.entity.article.UploadFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TestArticleDTO {
    private String title;
    private String content;
    private List<MultipartFile> imageFiles;
}
