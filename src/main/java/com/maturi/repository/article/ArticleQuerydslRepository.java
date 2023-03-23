package com.maturi.repository.article;

import com.maturi.dto.article.search.ArticleSearchCond;
import com.maturi.dto.article.search.MySliceImpl;
import com.maturi.entity.article.Article;
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

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.article.QLikeArticle.likeArticle;
import static com.maturi.entity.article.QTagValue.tagValue;
import static com.maturi.util.constfield.LocationConst.kmToLat;

@RequiredArgsConstructor
@Repository
public class ArticleQuerydslRepository {
    //querydsl을 쓰기위한 객체
    private final JPAQueryFactory query;

    //유저가 좋아요를 누른 게시글을을 찾음
    public List<Article> findByLike(Long memberId) {//테스트함
        //select a from LikeArticle l join article a on l.article_id = a.id where l.member_id = ?;
        return query
                .select(article)
                .from(likeArticle)
                .join(likeArticle.article, article).fetchJoin()
                .where(likeArticle.member.id.eq(memberId))
                .fetch();
    }

    //유저가 입력한 키워드가 태그명의 일부에 포함되는 게시글을 찾음
    public List<Article> findByTagValue(String tag) {//테스트함
        //select a from TagValue t join Article a on t.article_id = a.id where tag like %?%;
        return query
                .select(article)
                .from(tagValue)
                .join(tagValue.article, article)
                .on(tagValue.tag.name.contains(tag))
                .fetch();
    }

    //페이징 처리한 동적쿼리문
    public MySliceImpl searchDynamicQueryAndPaging(Long lastArticleId,
                                                      ArticleSearchCond cond,
                                                      Pageable pageable) {
        //where문을 보면 ,로 구분이 되었는데 이는 and조건이므로 or로 조건을 걸어야하는 키워드검색은
        //BooleanBuilder 객체를 사용해서 조건들을 체이닝해준다.
        //BooleanBuilder객체를 사용하지 않고 체이닝을 하면 제일 앞에있는 조건의 값이 null일경우 에러가 발생하게된다.
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(contentLike(cond.getContent()))//글 내용 keyword검색
                .or(writerLike(cond.getWriter()))//작성자(닉네임) keyword검색
                .or(tagArticleIn(cond.getArticlesByTagValue()))//태그 keyword검색
                .or(restaurantNameLike(cond.getRestaurantName()));//음식점명 keyword검색

        List<Article> results = query.selectFrom(article)
                .where(
                        // no-offset 페이징 처리
                        ltStoreId(lastArticleId),

                        // 검색조건들
                        followMembersIn(cond.getFollowMembers()),//팔로우한 유저로 검색
                        sidoEq(cond.getSido()),//시도로 검색
                        sigoonEq(cond.getSigoon()),//시군으로 검색
                        dongEq(cond.getDong()),//동으로 검색
                        latitudeBetween(cond.getLatitude()),//위도로 검색
                        longitudeBetween(cond.getLongitude()),//경도로 검색
                        categoryEq(cond.getCategory()),//음식점 카테고리로 검색
                        likeArticleIn(cond.getLikeArticles()),//좋아요누른 게시판 검색
                        builder//keyword조건 검색
                )
                .orderBy(article.id.desc())//아이디가 높은 것(최신순)으로 내림차순
                .limit(pageable.getPageSize()+1)
                .fetch();

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
        }

        return new MySliceImpl(results,hasNext);
        // 무한 스크롤 처리
    }
    //페이징 처리를 하지않은 동적쿼리문
    public List<Article> searchBooleanBuilder(ArticleSearchCond cond) {

        //where문을 보면 ,로 구분이 되었는데 이는 and조건이므로 or로 조건을 걸어야하는 키워드검색은
        //BooleanBuilder 객체를 사용해서 조건들을 체이닝해준다.
        //BooleanBuilder객체를 사용하지 않고 체이닝을 하면 제일 앞에있는 조건의 값이 null일경우 에러가 발생하게된다.
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(contentLike(cond.getContent()))//글 내용 keyword검색
                .or(writerLike(cond.getWriter()))//작성자(닉네임) keyword검색
                .or(tagArticleIn(cond.getArticlesByTagValue()))//태그 keyword검색
                .or(restaurantNameLike(cond.getRestaurantName()));//음식점명 keyword검색

        return query.selectFrom(article)
                .where(
                        followMembersIn(cond.getFollowMembers()),//팔로우한 유저로 검색
                        sidoEq(cond.getSido()),//시도로 검색
                        sigoonEq(cond.getSigoon()),//시군으로 검색
                        dongEq(cond.getDong()),//동으로 검색
                        latitudeBetween(cond.getLatitude()),//위도로 검색
                        longitudeBetween(cond.getLongitude()),//경도로 검색
                        categoryEq(cond.getCategory()),//음식점 카테고리로 검색
                        likeArticleIn(cond.getLikeArticles()),//좋아요누른 게시판 검색
                        builder//keyword조건 검색
                )
                .orderBy(article.id.desc())//아이디가 높은 것(최신순)으로 내림차순
                .limit(20)
                .fetch();
    }


    //팔로우한 유저의 게시글들의 where절
    private BooleanExpression followMembersIn(List<Member> followMembers) {
        return followMembers != null && followMembers.size() > 0 ? article.member.in(followMembers) : null;
    }
    //관심지역(시도)에 있는 음식점의 게시글들의 where절
    private BooleanExpression sidoEq(String sido) {
        return StringUtils.hasText(sido) ? article.restaurant.area.sido.eq(sido) : null;
    }
    //관심지역(시군)에 있는 음식점의 게시글들의 where절
    private BooleanExpression sigoonEq(String sigoon) {
        return StringUtils.hasText(sigoon) ? article.restaurant.area.sigoon.eq(sigoon) : null;
    }
    //관심지역(동)에 있는 음식점의 게시글들의 where절
    private BooleanExpression dongEq(String dong) {
        return StringUtils.hasText(dong) ? article.restaurant.area.dong.eq(dong) : null;
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
        return StringUtils.hasText(category) && !category.equals("") ? article.restaurant.category.eq(category) : null;
    }
    //좋아요를 누른 게시글들
    private BooleanExpression likeArticleIn(List<Article> likeArticles) {
        return likeArticles != null && likeArticles.size() > 0 ? article.in(likeArticles) : null;
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
        return tagArticle != null && tagArticle.size() > 0 ? article.in(tagArticle) : null;
    }
    //음식점명 조건의의 keyword로 검색한 게시글들
    private BooleanExpression restaurantNameLike(String restaurant) {
        return StringUtils.hasText(restaurant) ? article.restaurant.name.contains(restaurant) : null;
    }

    // 무한 스크롤 방식 처리하는 메서드
    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltStoreId(Long articleId) {
        if (articleId == null) {
            return null;
        }
        return article.id.lt(articleId);
    }

}
