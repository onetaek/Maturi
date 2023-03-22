package com.maturi.repository.article;

import com.maturi.dto.article.ArticleSearchCond;
import com.maturi.dto.article.ArticleSearchRequest;
import com.maturi.entity.article.Article;
import com.maturi.entity.member.Area;
import com.maturi.service.article.ArticleService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.article.QTagValue.tagValue;
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
    @Autowired
    ArticleService articleService;
    @Autowired
    JPAQueryFactory query;

    @Test
    @DisplayName("카테고리로 한식을 선택하고 키워드에 null대신 쌍따옴표 입력")
    void 한식선택(){
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .followMembers(null)
                .sido(null)
                .sigoon(null)
                .dong(null)
                .latitude(null)
                .longitude(null)
                .category("한식")
                .content("")
                .writer(null)
                .articlesByTagValue(null)
                .restaurantName(null)
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
    @DisplayName("관심지역을 대구>동구로 검색")
    void 관심지역을_대구_동구로검색(){
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .sido("대구광역시")
                .sigoon("동구")
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getRestaurant().getArea().getSido()).isEqualTo("대구광역시");
            assertThat(findArticle.getRestaurant().getArea().getSigoon()).isEqualTo("동구");
        }
    }


    @Test
    @DisplayName("글내용으로 키워드 검색")
    void 키워드_글내용_검색(){
        String content = "of type and scrambled it to make a type specimen book. It h";
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .content(content)
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getContent()).contains(content);
        }
    }

    @Test
    @DisplayName("닉네임으로 키워드 검색")
    void 닉네임_키워드_검색(){
        String keyword = "559";
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .writer(keyword)
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getMember().getNickName()).contains(keyword);
        }
    }

    @Test
    @DisplayName("해새태그 키워드 검색")
    void 해시태그_키워드_검색(){
        String keyword = "그722";

        List<Article> articls = articleQRepository.findByTagValue(keyword);
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .articlesByTagValue(articls)
                .build();

        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle).isEqualTo(cond.getArticlesByTagValue());
        }
    }

    @Test
    void simpleTest(){
        List<Article> articles = query
                .select(article)
                .from(article)
                .join(tagValue.article, article).fetchJoin()
                .fetch();
        log.info("articles={}",articles);
    }
}