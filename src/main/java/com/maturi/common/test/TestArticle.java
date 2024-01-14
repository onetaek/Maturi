package com.maturi.common.test;

import lombok.Data;

import java.util.List;

@Data
public class TestArticle {
    private Long id;
    private String title;
    private String content;
    private List<String> imageFiles;
}
