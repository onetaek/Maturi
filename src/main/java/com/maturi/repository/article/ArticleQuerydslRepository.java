package com.maturi.repository.article;

import com.maturi.dto.article.ArticleSearchCond;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Area;
import com.maturi.entity.member.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Supplier;

import static com.maturi.entity.article.QArticle.*;
import static com.maturi.entity.article.QLikeArticle.*;
import static com.maturi.entity.article.QTagValue.*;
import static com.maturi.util.constfield.LocationConst.*;

@RequiredArgsConstructor
@Repository
public class ArticleQuerydslRepository {

    private final JPAQueryFactory query;

    public List<Article> findByLike(Long memberId){
        //select a from LikeArticle l join article a on l.article_id = a.id where l.member_id = ?;
        return query
                .select(article)
                .from(likeArticle)
                .join(likeArticle.article, article).fetchJoin()
                .where(likeArticle.member.id.eq(memberId))
                .fetch();
    }

    public List<Article> findByTagValue(String tag){
        //select a from TagValue t join Article a on t.article_id = a.id where tag like %?%;
        return query
                .select(article)
                .from(tagValue.article)
                .join(tagValue.article, article).fetchJoin()
                .where(tagValue.tag.name.contains(tag))
                .fetch();
    }

    public List<Article> searchBySlice2(Long lastArticleId,
                                        ArticleSearchCond cond) {
        List<Article> results = query.selectFrom(article)
                .where(
                    radioBtnSearchCond(cond)//라디로 버튼 검색 조건들
                    .and(keywordSearchCond(cond))//keyword로 검색 조건들
                )
                .orderBy(article.id.desc())//아이디가 높은 것(최신순)으로 내림차순
//                .limit(pageable.getPageSize()+1)
                .limit(10)
                .fetch();

        // 무한 스크롤 처리
//        return checkLastPage(pageable, results);
        return results;
    }

    public List<Article> searchBySlice(ArticleSearchCond cond) {
        return query.selectFrom(article)
                .where(
                    radioBtnSearchCond(cond)//라디로 버튼 검색 조건들
                    .and(keywordSearchCond(cond))//keyword로 검색 조건들
                )
                .orderBy(article.id.desc())//아이디가 높은 것(최신순)으로 내림차순
                .fetch();
    }

    private BooleanBuilder radioBtnSearchCond(ArticleSearchCond cond){
        return followMembersIn(cond.getFollowMembers())
                .and(areaEq(cond.getArea()))
                .and(locationBetween(cond.getLatitude(),cond.getLongitude()))
                .and(categoryEq(cond.getCategory()))
                .and(likeArticleIn(cond.getLikeArticles()));
    }

    private BooleanBuilder keywordSearchCond(ArticleSearchCond cond){
        return contentLike(cond.getContent())
                .or(writerLike(cond.getWriter()))
                .or(tagArticleIn(cond.getArticlesByTagValue()))
                .or(restaurantNameLike(cond.getRestaurantName()));
    }

    //팔로우한 유저의 게시글들의 where절
    private BooleanBuilder followMembersIn(List<Member> followMembers) {
        return nullSafeBuilder(() -> article.member.in(followMembers));
    }

    //관심지역에 있는 음식점의 게시글들의 where절
    private BooleanBuilder areaEq(Area area){
        return nullSafeBuilder(() -> article.restaurant.area.eq(area));
    }

    //현재 내위치주변에 있는 음식점의 게시글들의 where절
    private BooleanBuilder locationBetween(Double latitude, Double longitude){
        return nullSafeBuilder(() ->
                article.restaurant.location.latitude.between(latitude - kmToLat,latitude + kmToLat)
                        .and(article.restaurant.location.longitude.between(longitude - kmToLat,longitude + kmToLat)));
    }

    //음식점 카테고리가 일치하는 게시글들의 where절
    private BooleanBuilder categoryEq(String category){
        return nullSafeBuilder(()-> article.restaurant.category.eq(category));
    }

    //좋아요를 누른 게시글들
    private BooleanBuilder likeArticleIn(List<Article> likeArticles){
        return nullSafeBuilder(()->article.in(likeArticles));
    }

    //글 내용조건의 keyword로 검색한 게시글들
    private BooleanBuilder contentLike(String content){
        return nullSafeBuilder(()->article.content.contains(content));
    }

    //작성자 조건의의 keyword로 검색한 게시글들
    private BooleanBuilder writerLike(String content){
        return nullSafeBuilder(()->article.member.nickName.contains(content));
    }

    //태그명 조건의 keyword로 검색한 게시글들
    private BooleanBuilder tagArticleIn(List<Article> tagArticle){
        return nullSafeBuilder(()->article.in(tagArticle));
    }

    //음식점명 조건의의 keyword로 검색한 게시글들
    private BooleanBuilder restaurantNameLike(String content){
        return nullSafeBuilder(()->article.restaurant.name.contains(content));
    }

    public static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }

    // no-offset 방식 처리하는 메서드
    private BooleanBuilder ltArticleId(Long articleId) {
        return nullSafeBuilder(()->article.id.lt(articleId));
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<Article> checkLastPage(Pageable pageable, List<Article> results) {
        boolean hasNext = false;
        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }

}
