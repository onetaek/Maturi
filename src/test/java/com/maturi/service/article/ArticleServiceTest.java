package com.maturi.service.article;

import com.maturi.entity.article.*;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.entity.member.QMember;
import com.maturi.repository.article.ArticleRepository;
import com.maturi.repository.member.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Projection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.maturi.entity.article.QArticle.*;
import static com.maturi.entity.article.QRestaurant.*;
import static com.maturi.entity.article.QTag.*;
import static com.maturi.entity.article.QTagValue.*;
import static com.maturi.entity.member.QMember.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Transactional
@SpringBootTest
class ArticleServiceTest {

    @Autowired
    JPAQueryFactory queryFactory;
    @PersistenceContext
    EntityManager em;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MemberRepository memberRepository;


    @BeforeEach
    public void before(){
        //회원가입
        Member member1 = Member.builder()
                .email("test1@naver.com")
                .passwd("1234")
                .salt("난수1")
                .name("오원택")
                .nickName("@user-12312412")
                .status(MemberStatus.NORMAL)
                .build();
        em.persist(member1);

        //글작성
        Location location = Location.builder()
                .oldAddress("구주소1")
                .address("주소1")
                .latitude(35.123)
                .longitude(36.4543)
                .build();

        Restaurant restaurant1 = Restaurant.builder()
                .name("맘스터치 동호점")
                .category("패스트푸드")
                .location(location)
                .build();
        em.persist(restaurant1);

        UploadFile uploadFile = UploadFile.builder()
                .uploadFileName("이미지1.png,이미지2.png")
                .storeFileName("13214124.ppng,dsa42132.png")
                .build();

        Article article1 = Article.builder()
                .member(member1)
                .restaurant(restaurant1)
                .content("리뷰글의 내용")
                .uploadFile(uploadFile)
                .status(ArticleStatus.NORMAL)
                .build();
        em.persist(article1);

        List<Article> findArticles = articleRepository.findAll();
        log.info("findArticles = {}",findArticles);

        Tag tag1 = Tag.builder()
                .name("태그1")
                .build();
        Tag tag2 = Tag.builder()
                .name("태그2")
                .build();
        em.persist(tag1);
        em.persist(tag2);

        TagValue tagValue1 = TagValue.builder()
                .tag(tag1)
                .article(article1)
                .build();
        em.persist(tagValue1);

        em.flush();
        em.clear();
    }

    @Test
    void jpa_findAll(){
        List<Article> findArticles = articleRepository.findAll();
        log.info("findArticles={}",findArticles);
    }

    @Test
    void jpql_findAll(){
        List<Article> findArticles = em.createQuery("select a from Article a", Article.class)
                .getResultList();
        log.info("findArticles={}",findArticles);
    }

    @Test
    void querydsl_findAll(){
        queryFactory
                .selectFrom(article)
                .fetchResults();
    }

    @Test
    void article_join_tagValue(){
        List<Article> findArticle = queryFactory
                .select(article)
                .from(article)
                .join(article.member, member)
                .join(article.restaurant,restaurant)
                .join(article.tagValue, tagValue).fetchJoin()
                .where()
                .fetch();
        for (Article article : findArticle) {
            log.info("article={}",article);
        }
    }

    private BooleanExpression contentLike(String content){
        return hasText(content) ? article.content.like(content) : null;
    }

    private BooleanExpression writerLike(String writer){
        return hasText(writer) ? article.member.name.like(writer) : null;
    }

//    private BooleanExpression tagLike(String tag){
//        return hasText(tag) ? article.tagValue : null;
//    }

    private BooleanExpression restaurantNameLike(String restaurantName){
        return hasText(restaurantName) ? article.restaurant.name.like(restaurantName) : null;
    }

    private BooleanExpression keywordSearchCond(String content, String writer, String restaurantName){
        return contentLike(content).and(writerLike(writer)).and(restaurantNameLike(restaurantName));
    }



}