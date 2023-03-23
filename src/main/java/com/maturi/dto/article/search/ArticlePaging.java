package com.maturi.dto.article.search;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ArticlePaging {

    Object content;
    boolean hasNext;
    private String event;
    private Long lastArticleId;
    public ArticlePaging(Object content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}
