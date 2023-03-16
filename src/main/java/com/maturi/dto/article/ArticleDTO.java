package com.maturi.dto.article;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    @NotBlank
    private String title;
    private String content;
    private String tags;
    //나중에추가
    private String image;

    private String restaurantName;
    private String category;
    private String oldAddress;
    private String address;
    private Double latitude;
    private Double longitude;
}
