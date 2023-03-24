package com.maturi.dto.article.search;

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

    public void setContent(List<ArticleViewDTO> articleViewDTOS) {
        this.content = (List<T>) articleViewDTOS;
    }
}
