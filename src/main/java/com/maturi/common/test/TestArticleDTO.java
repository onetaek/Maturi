package com.maturi.common.test;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TestArticleDTO {
    private String title;
    private String content;
    private List<MultipartFile> imageFiles;
}
