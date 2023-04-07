package com.maturi.dto.article;

import com.maturi.entity.member.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMyPageViewDTO {

    private Long id;
    private String image;
    private int likeCount;
    private int commentCount;
    private String restaurantName;
    private String sido;
    private String sigoon;
    private String dong;
}
