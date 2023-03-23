package com.maturi.repository.article;

import com.maturi.dto.article.search.ArticleSearchCond;
import com.maturi.entity.article.Article;
import com.maturi.entity.member.Member;
import com.maturi.service.article.ArticleService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.article.QLikeArticle.likeArticle;
import static com.maturi.entity.article.QTagValue.tagValue;
import static com.maturi.entity.member.QFollow.follow;
import static com.maturi.entity.member.QMember.member;
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
    JPAQueryFactory query;

    @Test
    @DisplayName("아무 조건 없이 전체 게시글조회")
    void 전체조회(){
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .build();
        log.info("cond={}",cond);
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("findArticle = {}",findArticle);
        }
    }

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
    @DisplayName("관심지역을 대구>동구이면서 글 내용 키워드로 검색")
    void 관심지역을_대구_동구_키워드_글내용검색(){
        String keyword = "y";
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .sido("대구광역시")
                .sigoon("동구")
                .content(keyword)
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getRestaurant().getArea().getSido()).isEqualTo("대구광역시");
            assertThat(findArticle.getRestaurant().getArea().getSigoon()).isEqualTo("동구");
            assertThat(findArticle.getContent()).contains(keyword);
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
        String keyword = "태그22459";

        List<Article> articles = articleQRepository.findByTagValue(keyword);

        ArticleSearchCond cond = ArticleSearchCond.builder()
                .articlesByTagValue(articles)
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        assertThat(findArticles.size()).isEqualTo(articles.size());//불확실한 검증 -> DB바뀌면 실패
        for (Article findArticle : findArticles) {
            log.info("findArticle = {}",findArticle);
        }
    }

    @Test
    @DisplayName("가게명으로 키워드 검색")
    void 가게명_키워드_검색(){
        String keyword = "name7";
        ArticleSearchCond cond = ArticleSearchCond.builder()
                .restaurantName(keyword)
                .build();
        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        for (Article findArticle : findArticles) {
            log.info("article={}",findArticle);
            assertThat(findArticle.getRestaurant().getName()).contains(keyword);
        }
    }

    @Test
    @DisplayName("팔로우하고 있는 유저의 게시글을 받음")
    void 팔로우_유저_검색(){

        List<Member> followMembers = query
                .select(follow.followerMember)
                .from(follow)
                .join(follow.followingMember, member)
                .on(follow.followingMember.id.eq(1L))
                .fetch();

        ArticleSearchCond cond = ArticleSearchCond.builder()
                .followMembers(followMembers)
                .build();

        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        List<Member> members = new ArrayList<>();
        for(Article a : findArticles){
            members.add(a.getMember());
        }
        for (Article findArticle : findArticles) {
            log.info("findArticle = {}",findArticle);
        }
        for (Member followMember : followMembers) {
            log.info("followMember = {}",followMember);
            Assertions.assertThat(members).contains(followMember);
        }
    }

    @Test
    @DisplayName("팔로우하고 있는 유저이면서 작성자 키워드로 게시글 검색")
    void 팔로우_유저_작성자_키워드_검색(){
        String keyword = "r-6";

        List<Member> followMembers = query
                .select(follow.followerMember)
                .from(follow)
                .join(follow.followingMember, member)
                .on(follow.followingMember.id.eq(1L))
                .fetch();

        ArticleSearchCond cond = ArticleSearchCond.builder()
                .followMembers(followMembers)
                .writer(keyword)
                .build();

        List<Article> findArticles = articleQRepository.searchBooleanBuilder(cond);
        List<Member> followMemberAndKeyWord = new ArrayList<>();
        for(Article a : findArticles){
            followMemberAndKeyWord.add(a.getMember());
        }
        for (Article findArticle : findArticles) {
            log.info("findArticle = {}, findArticle.getNickName() = {}",
                    findArticle,findArticle.getMember().getNickName());
        }
        for(Member m : followMemberAndKeyWord){
            log.info("팔로우 유저들의 닉네임 = {}",m.getNickName() );
            Assertions.assertThat(m.getNickName()).contains(keyword);
        }

        for(Member follwoAndKeywordMember : followMemberAndKeyWord){
            assertThat(followMembers).contains(follwoAndKeywordMember);
        }
    }

    @Test
    @DisplayName("좋아요를 누른 게시글들 검색")
    void 좋아요누른_게시글검색(){
        List<Article> findArticle = query
                .select(article)
                .from(likeArticle)
                .join(likeArticle.article, article)
                .where(likeArticle.member.id.eq(1L))
                .fetch();

        ArticleSearchCond cond = ArticleSearchCond.builder()
                .likeArticles(findArticle)
                .build();

        List<Article> articles = articleQRepository.searchBooleanBuilder(cond);
        for (Article article : articles) {
            log.info("article = {}",article);
        }
        assertThat(findArticle.size()).isEqualTo(articles.size());
    }

    @Test
    @DisplayName("좋아요를 누른 게시글이면서 키워드 내용으로 검색")
    void 좋아요누른_게시글_키워드_내용_검색(){
        String keyword = "industry's";
        List<Article> findArticles = query
                .select(article)
                .from(likeArticle)
                .join(likeArticle.article, article)
                .where(likeArticle.member.id.eq(1L))
                .fetch();
        List<Member> findArticlesMember = new ArrayList<>();
        for (Article findArticle : findArticles) {
            findArticlesMember.add(findArticle.getMember());
        }

        ArticleSearchCond cond = ArticleSearchCond.builder()
                .likeArticles(findArticles)
                .content(keyword)
                .build();
        List<Article> articles = articleQRepository.searchBooleanBuilder(cond);
        List<Member> articlesMember = new ArrayList<>();
        for (Article article : articles) {
            articlesMember.add(article.getMember());
        }
        for (Article article : articles) {
            log.info("article = {}",article);
            assertThat(article.getContent()).contains(keyword);
        }
        //articles의 member들이 모두 findArticles의 member에 포함되어야한다.
    }

    @Test
    @DisplayName("태그를 가지고있는 게시글을 받음")
    void simpleTest(){
    String keyword = "태그22459";
        List<Article> articles = query
                .select(article)
                .from(tagValue)
                .join(tagValue.article, article)
                .on(tagValue.tag.name.contains(keyword))
                .fetch();
        for (Article article1 : articles) {
            log.info("article = {}",article1);
        }
    }

}