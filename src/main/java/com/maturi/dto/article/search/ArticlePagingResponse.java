package com.maturi.dto.article.search;

import com.maturi.dto.article.ArticleMyPageViewDTO;
import com.maturi.dto.article.ArticleViewDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticlePagingResponse<T> {

    private List<T> content = new ArrayList<>();
    boolean hasNext;
    private String event;
    private Long lastArticleId;
    public ArticlePagingResponse(List<T> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
    //메인페이지에서 사용할 게시글 DTO
    public void setContent(List<ArticleViewDTO> articleViewDTOS) {
        this.content = (List<T>) articleViewDTOS;
    }
    //마이페이지에서 사용할 게시글 DTO
    public void setContentForMyPage(List<ArticleMyPageViewDTO> articleMyPageViewDTOS){
        this.content = (List<T>) articleMyPageViewDTOS;
    }
}
