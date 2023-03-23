package com.maturi.service.article;

import com.maturi.dto.article.ArticleSearchRequest;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Area;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.repository.article.ArticleRepository;
import com.maturi.repository.member.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.article.QRestaurant.restaurant;
import static com.maturi.entity.article.QTagValue.tagValue;
import static com.maturi.entity.member.QMember.member;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
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
                .salt("salt1")
                .name("one taek")
                .nickName("@user-12312412")
                .status(MemberStatus.NORMAL)
                .build();
        em.persist(member1);
        //글작성
        Location location = Location.builder()
                .oldAddress("oldAddress1")
                .address("address1")
                .latitude(35.123)
                .longitude(36.4543)
                .build();

        Restaurant restaurant1 = Restaurant.builder()
                .name("restaurant name")
                .category("korean food")
                .location(location)
                .build();
        em.persist(restaurant1);
//        UploadFile uploadFile = UploadFile.builder()
//                .uploadFileName("이미지1.png,이미지2.png")
//                .storeFileName("13214124.ppng,dsa42132.png")
//                .build();
//
        Article article1 = Article.builder()
                .member(member1)
                .restaurant(restaurant1)
                .content("testtest")
                .image("image name1")
                .status(ArticleStatus.NORMAL)
                .build();
        em.persist(article1);
        List<Article> findArticles = articleRepository.findAll();
        log.info("findArticles = {}",findArticles);

        Tag tag1 = Tag.builder()
                .name("tag1")
                .build();
        Tag tag2 = Tag.builder()
                .name("tag2")
                .build();
        em.persist(tag1);
        em.persist(tag2);
//        TagValue tagValue1 = TagValue.builder()
//                .tag(tag1)
//                .article(article1)
//                .build();
//        em.persist(tagValue1);

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

    @Test
    void searchTest(){
        String radioCond = "";//ex)all,follow,local,my-local,category -> where문에 사용안됨


        Double latitude =null;
        Double longitude =null;
        String category ="";//ex) 한식, 중식, 패스트푸드
        String follow = "";
        Area area = Area.builder()
                .sido("대구")
                .sigoon("동구")
                .dong("신서동")
                .build();

        String keyword ="검색 키워드";

        String content =keyword;
        String writer =keyword;
//        String tag =keyword;
        Integer[] ids = {1,2,3,4,5};
        List<Integer> ids2 = new ArrayList<>();
        ids2.add(1);
        ids2.add(2);
        ids2.add(3);
        String restaurantName =keyword;

        ArticleSearchRequest articleSearchRequest = ArticleSearchRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .category(category)
                .content(content)
                .writer(writer)
                .tag(null)
                .restaurantName(restaurantName)
                .build();

        BooleanBuilder builder = new BooleanBuilder();//초기화 값을 넣을 수도 있음
        //라디로버튼 검색
        if(latitude != null && longitude != null){//위도경도
            builder.and(article.restaurant.location.latitude.between(12,21));
            builder.and(article.restaurant.location.longitude.between(01,13));
        }
        if(StringUtils.hasText(category)){//음식점 카테고리
            builder.and(article.restaurant.category.eq(category));
        }
        if(ids.length != 0){//팔로우 유저 확인
            builder.and(article.member.id.in(1,2,3,4,5));
        }
        if(area != null){//관심지역
            builder.and(article.restaurant.area.sido.eq(area.getSido()));
            builder.and(article.restaurant.area.sigoon.eq(area.getSigoon()));
            builder.and(article.restaurant.area.dong.eq(area.getDong()));
        }
        if(ids.length!=0){//ids는 임시로 해놓은거고 실제론 내가 like누른 게시글의 id를 가져와야함
            builder.and(article.id.in(ids));
        }

        //키워드 검색
        if(StringUtils.hasText(content)){//글 내용
            builder.or(article.content.contains(keyword));// %keyword% 로 like함수 실행
        }
        if(ids.length != 0){//해시태그
            builder.or(article.id.in(ids));// in(~,~,~)함수 실행
        }
        if(StringUtils.hasText(writer)){//작성자
            builder.or(article.member.name.contains(writer));// %keyword% 로 like함수 실행
        }
        if(StringUtils.hasText(restaurantName)){//음식점 명
            builder.or(article.restaurant.name.contains(restaurantName));// %keyword% 로 like함수 실행
        }

        QueryResults<Article> results = queryFactory
                .select(article)
                .from(article)
                .join(article.member, member)
                .join(article.restaurant, restaurant)
                .join(article.tagValue, tagValue).fetchJoin()
                .where(builder)
                .limit(12)
                .fetchResults();

        List<Article> findArticleSearchPaging = results.getResults();
        long total = results.getTotal();//이건 우린 필요없음

    }

    @Test
    void builderNullTest(){
        BooleanBuilder builder = new BooleanBuilder();//초기화 값을 넣을 수도 있음
        builder.and(null);

        List<Member> findMember = queryFactory.select(member)
                .from(member)
                .where(builder)
                .fetch();
        log.info("findMember={}",findMember);
    }

    @Test
    void multiFunctionInWhere(){
        String username = "test";
        String userEmail = null;

        List<Member> findMemberByIdAndEmail = queryFactory
                .selectFrom(member)
                .where(allEq(username, userEmail))
                .fetch();
        log.info("findMemberByIdAndEmail={}",findMemberByIdAndEmail);

        Member member1 = Member.builder().build();

        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member1);
        members.add(member1);
        members.add(member1);

        List<Member> findMemberByIdAndEmail1 = queryFactory
                .selectFrom(member)
                .where(member.in(members))
                .fetch();

    }

    private BooleanExpression usernameEq(String usernameCond){
        return usernameCond != null ? member.name.eq(usernameCond) : null;
    }

    private BooleanExpression userEmailEq(String userEmailCond){
        return userEmailCond != null ? member.email.eq(userEmailCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, String userEmailCond){
        return usernameEq(usernameCond).and(userEmailEq(userEmailCond));
    }

    @Test
    @DisplayName("객체들 끼리 비교할때 참조 값이 달라도 값이 같을까?")
    void entitySameTest(){
        //전체 게시글 조회
        List<Article> articles = articleRepository.findAll();
        //게시글들중 첫번째꺼 선택
        Article findArticle = articles.get(0);
        //게시글에 등록된 member의 id를 get
        Long memberId = findArticle.getMember().getId();
        //id를 기반으로 member를 찾음
        Member findMember = memberRepository.findById(memberId).orElse(null);

        log.info("findMember={}",findMember);
        log.info("findArticle={}",findArticle);

        assertThat(findMember).isEqualTo(findArticle.getMember());
    }

    @Test
    void distanceCal(){
        //지하철역 8개 (성서산업단지역 ~ 청라언덕역 기준) = 7.1km(차로 이동했을 때 최단경로)
        //위도 1도 : 111.1412Km = ? : 7.1km
        log.info("7.1km / 2 는 위도로로 몇일까? = {}",7.1/111.1412/2);//0.03194135028234354
        log.info("test={}",Math.random()/100);
    }


}