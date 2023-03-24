package com.maturi.dto.article.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticlePagingRequest {
    /**
     * paging에 필요한 정보들
     */
    private Long lastArticleId;//가장 아래에 있는 게시글
    private String event;//ex) scroll, load
    private int size;//한번에 가져올 게시글 갯수
}
