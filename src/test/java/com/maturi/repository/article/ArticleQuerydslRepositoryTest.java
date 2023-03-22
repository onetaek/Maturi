package com.maturi.repository.article;

import com.maturi.dto.article.ArticleSearchCond;
import com.maturi.entity.article.Article;
import com.maturi.entity.member.Area;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;

@Slf4j
@Transactional
@SpringBootTest
class ArticleQuerydslRepositoryTest {
    @Autowired
    ArticleQuerydslRepository articleQRepository;
    @Autowired
    ArticleRepository articleRepository;

    @Test
    @DisplayName("카테고리로 한식을 선택하고 키워드에 null대신 쌍따옴표 입력")
    void 한식선택(){
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .category("한식")
                .content("")
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getRestaurant().getCategory()).isEqualTo("한식");
        }
    }

    @Test
    @DisplayName("카테고리로 중식을 선택하고, keyword에 띄어쓰기를 입력")
    void 중식선택(){
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .category("중식")
                .content("   ")
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getRestaurant().getCategory()).isEqualTo("중식");
        }
    }

    @Test
    @DisplayName("카테고리로 중식을 선택하고, keyword에 띄어쓰기를 입력")
    void 선택(){
        Area area = Area.builder()
                .sido("대구")
                .sigoon("동구")
                .build();

        ArticleSearchCond cond = ArticleSearchCond.builder()
                .followMembers(null)
                .area(area)
                .latitude(null)
                .longitude(null)
                .category("")
                .content("")
                .writer(null)
                .articlesByTagValue(null)
                .restaurantName(null)
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getRestaurant().getArea().getSido()).isEqualTo("대구");
            assertThat(findArticle.getRestaurant().getArea().getSigoon()).isEqualTo("동구");
        }
    }




}