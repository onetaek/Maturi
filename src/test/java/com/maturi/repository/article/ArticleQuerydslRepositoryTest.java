package com.maturi.repository.article;

import com.maturi.dto.article.ArticleSearchCond;
import com.maturi.entity.article.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@Transactional
@SpringBootTest
class ArticleQuerydslRepositoryTest {
    @Autowired
    ArticleQuerydslRepository articleQRepository;
    @Autowired
    ArticleRepository articleRepository;

    @Test
    @DisplayName("제발")
    void searchTest1(){

        ArticleSearchCond cond = ArticleSearchCond.builder()
                .category("한식")
                .content("")
                .build();
        List<Article> findArticles = articleQRepository.searchBySlice(cond);
        log.info("findArticles={}",findArticles);
//        articleQRepository.searchBySlice();

    }

    @Test
    @DisplayName("모두 찾아")
    void findAll(){
        List<Article> all = articleRepository.findAll();
        for (Article article : all) {
            log.info("id = {}",article.getId());
            log.info("member = {}",article.getMember());
            log.info("restaurant = {}",article.getRestaurant());
            log.info("content = {}",article.getContent());
            log.info("image = {}",article.getImage());
            log.info("status = {}",article.getStatus());
        }
    }


}