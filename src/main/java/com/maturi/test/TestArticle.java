package com.maturi.test;

import com.maturi.entity.article.UploadFile;
import lombok.Data;

import java.util.List;

@Data
public class TestArticle {
    private Long id;
    private String title;
    private String content;
    private List<UploadFile> imageFiles;
}
