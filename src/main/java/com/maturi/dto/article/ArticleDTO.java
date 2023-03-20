package com.maturi.dto.article;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private String content;
    private String tags;
    //나중에추가
    private String image;

    private String name; // 음식점 이름
    private String category;
    private String oldAddress;
    private String address;
    private Double latitude;
    private Double longitude;
}
