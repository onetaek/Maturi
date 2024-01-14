package com.maturi.repository.article;

import com.maturi.dto.member.MemberFollowResponse;
import com.maturi.entity.article.*;
import com.maturi.entity.like.LikeArticle;
import com.maturi.entity.member.Member;
import com.maturi.repository.restaurant.RestaurantQuerydslRepository;
import com.maturi.repository.member.follow.FollowQuerydslRepository;
import com.maturi.service.article.ArticleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.article.QLikeArticle.*;
import static org.assertj.core.api.Assertions.assertThat;

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
    RestaurantQuerydslRepository restaurantQRepository;
    @Autowired
    JPAQueryFactory query;
    @Autowired
    FollowQuerydslRepository followQRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init(){
        Member member1 = Member.builder()
                .email("member1@naver.com")
                .name("member1")
                .nickName("@member1")
                .build();
        Member member2 = Member.builder()
                .email("member2@naver.com")
                .name("member2")
                .nickName("@member2")
                .build();
        Member member3 = Member.builder()
                .email("member3@naver.com")
                .name("member3")
                .nickName("@member3")
                .build();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        Article article1 = Article.builder()
                .image("image1")
                .content("content1")
                .status(ArticleStatus.NORMAL)
                .member(member1)
                .build();

        Article article2 = Article.builder()
                .image("image2")
                .content("content2")
                .status(ArticleStatus.NORMAL)
                .member(member2)
                .build();

        Article article3 = Article.builder()
                .image("image3")
                .content("content3")
                .status(ArticleStatus.NORMAL)
                .member(member3)
                .build();
        em.persist(article1);
        em.persist(article2);
        em.persist(article3);
    }

    @Test
    @DisplayName("팔로워들(나를 팔로잉하고있는 사람들)을 출력")
    void findByFollowingMemberId() {
        //한번 성공
        List<MemberFollowResponse> byFollowingMemberId = followQRepository.findFollowers(1L,null);
        log.info("팔로워들이 없나요? = {}",byFollowingMemberId == null || byFollowingMemberId.size() == 0);
        for (MemberFollowResponse memberFollowResponse : byFollowingMemberId) {
            log.info("member = {}",memberFollowResponse);
        }
    }

    @Test
    @DisplayName("팔로잉하고 있는 유저를 출력")
    void findByFollowerMember() {
        List<MemberFollowResponse> byFollowerMember = followQRepository.findFollowings(1L, null);
        log.info("팔로잉하고 있는 유저들이 없나요? = {}",byFollowerMember == null || byFollowerMember.size() == 0);
        for (MemberFollowResponse memberFollowResponse : byFollowerMember) {
            log.info("member = {}",memberFollowResponse);
        }
    }

    @Test
    @DisplayName("")
    public void 좋아요눌렀을때정렬() throws Exception{
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "member1")
                .getSingleResult();
        Member member2 = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "member2")
                .getSingleResult();
        Member member3 = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "member3")
                .getSingleResult();

        Article article1 = em.createQuery("select a from Article a where a.content = :content",Article.class)
                .setParameter("content","content1")
                .getSingleResult();
        Article article2 = em.createQuery("select a from Article a where a.content = :content",Article.class)
                .setParameter("content","content2")
                .getSingleResult();
        Article article3 = em.createQuery("select a from Article a where a.content = :content",Article.class)
                .setParameter("content","content3")
                .getSingleResult();

        LikeArticle likeArticle1 = LikeArticle.builder()
                .article(article1)
                .member(member1)
                .build();
        LikeArticle likeArticle2 = LikeArticle.builder()
                .article(article2)
                .member(member1)
                .build();
        LikeArticle likeArticle3 = LikeArticle.builder()
                .article(article2)
                .member(member2)
                .build();
        LikeArticle likeArticle4 = LikeArticle.builder()
                .article(article2)
                .member(member3)
                .build();
        LikeArticle likeArticle5 = LikeArticle.builder()
                .article(article3)
                .member(member1)
                .build();
        LikeArticle likeArticle6 = LikeArticle.builder()
                .article(article3)
                .member(member2)
                .build();
        em.persist(likeArticle1);
        em.persist(likeArticle2);
        em.persist(likeArticle3);
        em.persist(likeArticle4);
        em.persist(likeArticle5);
        em.persist(likeArticle6);

        //article1 -> 좋아요 1개
        //article2 -> 좋아요 3개
        //article3 -> 좋아요 2개

        Integer likeCount = 10;
        Long lastArticleId = 10L;

        //when
        BooleanBuilder orderBuilder = new BooleanBuilder();

        JPAQuery<Article> query = new JPAQuery<>(em);
        List<Article> findArticles = query.select(article)
                .from(article)
                .leftJoin(article.likes, likeArticle)
                .groupBy(article.id)
                .having(
                        likeArticle.count().loe(likeCount),
                        article.id.loe(lastArticleId)
                )
                .orderBy(
                        likeArticle.count().desc(),
                        article.id.desc()
                )
                .fetch();

        //then
        for(int i = 0 ; i < findArticles.size() ; i++){
            if(i == 0) {
                Article findArticle = findArticles.get(i);
                String content = findArticle.getContent();
                Assertions.assertThat(content).isEqualTo("content2");
            }
            if(i == 1) {
                Article findArticle = findArticles.get(i);
                String content = findArticle.getContent();
                Assertions.assertThat(content).isEqualTo("content3");
            }
            if(i == 2) {
                Article findArticle = findArticles.get(i);
                String content = findArticle.getContent();
                Assertions.assertThat(content).isEqualTo("content1");
            }
        }
    }

    private BooleanExpression likeArticleCountLt(Integer likeCount) {
        return likeCount != null ? likeArticle.count().lt(likeCount) : null;
    }

    private BooleanExpression likeArticleCountEq(Integer likeCount) {
        return likeCount != null ? likeArticle.count().eq(Long.valueOf(likeCount)) : null;
    }

    private BooleanExpression articleIdLoe(Long articleId){
        return articleId != null ? article.id.loe(articleId) : null;
    }
}