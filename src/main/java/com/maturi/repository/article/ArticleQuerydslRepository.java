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
import org.springframework.util.StringUtils;

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

    public List<Article> findByLike(Long memberId) {
        //select a from LikeArticle l join article a on l.article_id = a.id where l.member_id = ?;
        return query
                .select(article)
                .from(likeArticle)
                .join(likeArticle.article, article).fetchJoin()
                .where(likeArticle.member.id.eq(memberId))
                .fetch();
    }

    public List<Article> findByTagValue(String tag) {
        //select a from TagValue t join Article a on t.article_id = a.id where tag like %?%;
        return query
                .select(article)
                .from(tagValue.article)
                .join(tagValue.article, article).fetchJoin()
                .where(tagValue.tag.name.contains(tag))
                .fetch();
    }

    public List<Article> searchBooleanBuilder(ArticleSearchCond cond) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.or(contentLike(cond.getContent()))
                .or(writerLike(cond.getWriter()))
                .or(tagArticleIn(cond.getArticlesByTagValue()))
                .or(restaurantNameLike(cond.getRestaurantName()));

        return query.selectFrom(article)
                .where(
                        followMembersIn(cond.getFollowMembers()),
                        areaEq(cond.getArea()),
                        latitudeBetween(cond.getLatitude()),
                        longitudeBetween(cond.getLongitude()),
                        categoryEq(cond.getCategory()),
                        likeArticleIn(cond.getLikeArticles()),
                        builder
                )
                .orderBy(article.id.desc())//아이디가 높은 것(최신순)으로 내림차순
                .limit(20)
                .fetch();
    }

    public List<Article> searchBySlice(ArticleSearchCond cond) {

        return query.selectFrom(article)
                .where(

                )
                .orderBy(article.id.desc())//아이디가 높은 것(최신순)으로 내림차순
                .fetch();
    }


    //팔로우한 유저의 게시글들의 where절
    private BooleanExpression followMembersIn(List<Member> followMembers) {
        return followMembers != null ? article.member.in(followMembers) : null;
    }

    //관심지역에 있는 음식점의 게시글들의 where절
    private BooleanExpression areaEq(Area area) {
        return area != null ? article.restaurant.area.eq(area) : null;
    }

    //현재 위치주면(위도) where절
    private BooleanExpression latitudeBetween(Double latitude) {
        return latitude != null ? article.restaurant.location.longitude.between(latitude - kmToLat, latitude + kmToLat) : null;
    }

    //현재 위치주면(경도) where절
    private BooleanExpression longitudeBetween(Double longitude) {
        return longitude != null ? article.restaurant.location.longitude.between(longitude - kmToLat, longitude + kmToLat) : null;
    }

    //음식점 카테고리가 일치하는 게시글들의 where절
    private BooleanExpression categoryEq(String category) {
        return category!=null ? article.restaurant.category.eq(category) : null;
    }

    //좋아요를 누른 게시글들
    private BooleanExpression likeArticleIn(List<Article> likeArticles) {
        return likeArticles != null ? article.in(likeArticles) : null;
    }

    //글 내용조건의 keyword로 검색한 게시글들
    private BooleanExpression contentLike(String content) {
        return StringUtils.hasText(content) ? article.content.contains(content) : null;
    }

    //작성자 조건의의 keyword로 검색한 게시글들
    private BooleanExpression writerLike(String writer) {
        return StringUtils.hasText(writer) ? article.member.nickName.contains(writer) : null;
    }

    //태그명 조건의 keyword로 검색한 게시글들
    private BooleanExpression tagArticleIn(List<Article> tagArticle) {
        return tagArticle != null ? article.in(tagArticle) : null;
    }

    //음식점명 조건의의 keyword로 검색한 게시글들
    private BooleanExpression restaurantNameLike(String restaurant) {
        return StringUtils.hasText(restaurant) ? article.restaurant.name.contains(restaurant) : null;
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
