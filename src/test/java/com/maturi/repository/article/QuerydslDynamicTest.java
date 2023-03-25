package com.maturi.repository.article;

import com.maturi.dto.article.search.ArticleSearchCond;
import com.maturi.entity.article.Article;
import com.maturi.entity.article.QArticle;
import com.maturi.service.article.ArticleService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.maturi.entity.article.QArticle.*;

@Slf4j
@Transactional
@SpringBootTest
public class QuerydslDynamicTest {
    @Autowired
    ArticleQuerydslRepository articleQRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleService articleService;
    @Autowired
    JPAQueryFactory query;

    @Test
    @DisplayName("아무 조건 없이 전체 게시글조회")
    void 전체조회(){
        List<Article> fetch = query.select(article)
                .from(article)
                .limit(10)
                .fetch();
        log.info("fetch = {} ",fetch);
    }
}
