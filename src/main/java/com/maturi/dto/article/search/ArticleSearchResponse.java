package com.maturi.dto.article.search;

import com.maturi.entity.article.TagValue;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleSearchResponse {
    private Long id;
    private String content;
    private List<String> images;
    private LocalDateTime modifiedDate;

    private String name;
    private String nickName;

    private List<String> tags;
    private int like;
    private boolean isLiked;
}
