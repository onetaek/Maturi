package com.maturi.dto.article;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private String content;
    private String tags;
    //나중에추가
    private List<MultipartFile> image;

    private String name; // 음식점 이름
    private String category;
    private String oldAddress;
    private String address;
    private Double latitude;
    private Double longitude;
}
