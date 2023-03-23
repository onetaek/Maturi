package com.maturi.dto.article.search;

import com.maturi.entity.article.Article;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MySliceImpl {

    List<Article> article = new ArrayList<>();
    boolean hasNext;
    public MySliceImpl(List<Article> article, boolean hasNext) {
        this.article = article;
        this.hasNext = hasNext;
    }
}
