package com.maturi.repository.article;

import com.maturi.dto.article.search.ArticleSearchCond;
import com.maturi.dto.article.search.ArticlePagingResponse;
import com.maturi.entity.article.Article;
import com.maturi.entity.article.ArticleStatus;
import com.maturi.entity.member.Member;
import com.maturi.util.constfield.OrderConst;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.article.QComment.*;
import static com.maturi.entity.article.QLikeArticle.likeArticle;
import static com.maturi.entity.article.QRestaurant.*;
import static com.maturi.entity.article.QTag.tag;
import static com.maturi.entity.article.QTagValue.tagValue;
import static com.maturi.entity.member.QMember.*;
import static com.maturi.util.constfield.LocationConst.kmToLat;
@RequiredArgsConstructor
@Repository
public class ArticleQuerydslRepository {
    //querydsl을 쓰기위한 객체
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    //유저가 좋아요를 누른 게시글을을 찾음
    public List<Article> findByLike(Long memberId) {//테스트함
        //select a from LikeArticle l join article a on l.article_id = a.id where l.member_id = ?;
        return queryFactory
                .select(article)
                .from(likeArticle)
                .join(likeArticle.article, article)
                .where(likeArticle.member.id.eq(memberId))
                .fetch();
    }

    //유저가 입력한 키워드가 태그명의 일부에 포함되는 게시글을 찾음
    public List<Article> findByTagValue(String inputTag) {//테스트함
        //select a from TagValue t join Article a on t.article_id = a.id where tag like %?%;
        return queryFactory
                .select(article)
                .from(tagValue)
                .join(tagValue.article, article)
                .join(tagValue.tag, tag)
                .on(tag.name.contains(inputTag))
                .fetch();
    }

    // WHERE article_id = ? AND (status = NORMAL OR status = REPORT) : 검색 가능한 리뷰글인지 확인 및 객체
    public Article findByIdAndStatus(Long articleId){
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(article.status.eq(ArticleStatus.NORMAL))
                .or(article.status.eq(ArticleStatus.REPORT));
        return queryFactory.selectFrom(article)
                .join(article.member, member)
                .join(article.restaurant, restaurant)
                .fetchJoin()
                .where(
                        articleIdEq(articleId),
                        builder
                )
                .fetchOne();//이거쓰면 하나만 가져올 수 있어요!
    }


    // 마이페이지 게시글 페이징 처리
    public ArticlePagingResponse<Article> findMyReviewArticles(Long memberId, // 마이페이지 회원 ID
                                                               Long lastArticleId,
                                                               int size){
        BooleanBuilder statusCond = new BooleanBuilder();
        statusCond.or(statusEq(ArticleStatus.NORMAL))// 상태가 NORMAL 게시글들만 출력
                .or(statusEq(ArticleStatus.REPORT));// 상태가 REPORT 게시글들만 출력

        List<Article> results = queryFactory.selectFrom(article)
                .join(article.member,member)
                .join(article.restaurant, restaurant)
                .fetchJoin()
                .where(
                        // no-offset 페이징 처리
                        ltStoreId(lastArticleId),
                        memberIdEq(memberId),
                        statusCond
                )
                .orderBy(article.id.desc())//아이디가 높은 것(최신순)으로 내림차순
                .limit(size+1)
                .fetch();

        boolean hasNext = false;
        if (results.size() > size) { // 다음 페이즈 있는지 확인
            hasNext = true;
            results.remove(results.size()-1);
        }

        return new ArticlePagingResponse<>(results,hasNext);
    }

    //페이징 처리한 동적쿼리문
    public ArticlePagingResponse<Article> searchDynamicQueryAndPaging(Long lastArticleId,
                                                                      ArticleSearchCond cond,
                                                                      String orderBy,
                                                                      int size) {
        JPAQuery<Article> query = new JPAQuery<>(em);

        //where문을 보면 ,로 구분이 되었는데 이는 and조건이므로 or로 조건을 걸어야하는 키워드검색은
        //BooleanBuilder 객체를 사용해서 조건들을 체이닝해준다.
        //BooleanBuilder객체를 사용하지 않고 체이닝을 하면 제일 앞에있는 조건의 값이
        //null일경우 에러가 발생하게되어서 or조건들은 BooleanBuilder에 체이닝을 해주었다.
        BooleanBuilder keywordCond = new BooleanBuilder();
        keywordCond.or(contentLike(cond.getContent()))//글 내용 keyword검색
                .or(nickNameLike(cond.getWriter()))//작성자(닉네임) keyword검색
                .or(nameLike(cond.getWriter()))//작성자(이름) keyword검색
                .or(tagArticleIn(cond.getArticlesByTagValue()))//태그 keyword검색
                .or(restaurantNameLike(cond.getRestaurantName()));//음식점명 keyword검색

        BooleanBuilder statusCond = new BooleanBuilder();
        statusCond.or(statusEq(ArticleStatus.NORMAL))// 상태가 NORMAL 게시글들만 출력
                .or(statusEq(ArticleStatus.REPORT));// 상태가 REPORT 게시글들만 출력

        query.from(article)
                .join(article.member,member)//article.member는 Article테이블에 있는 member_id, member는 Member테이블에 있는 id라고 생각
                .join(article.restaurant, restaurant)//article.restaurant는 Article테이블에 있는 restaurant_id, restaurant는 Restaurant테이블에 있는 id
                .fetchJoin()
                .where(
                        // no-offset 페이징 처리
                        ltStoreId(lastArticleId),
                        blockMemberIdNotIn(cond.getBlockedMemberIds()),
                        // 검색조건들
                        followMembersIn(cond.getFollowMembers()),//팔로우한 유저로 검색
                        sidoEq(cond.getSido()),//시도로 검색
                        sigoonEq(cond.getSigoon()),//시군으로 검색
                        dongEq(cond.getDong()),//동으로 검색
                        latitudeBetween(cond.getLatitude()),//위도로 검색
                        longitudeBetween(cond.getLongitude()),//경도로 검색
                        categoryEq(cond.getCategory()),//음식점 카테고리로 검색
                        likeArticleIn(cond.getLikeArticles()),//좋아요누른 게시판 검색
                        keywordCond,//keyword조건 검색
                        statusCond//게시글 상태조건 필터링
                )
                .limit(size + 1);//size를 DB에서 받는 것보다 프론트에서 받는게 더 유연할 것같음

        //정렬 동적 처리
        switch(orderBy){
            case OrderConst.CREATED_DATE_DESC:
                query.orderBy(article.createdDate.desc());
                break;
            case OrderConst.CREATED_DATE_ASC:
                query.orderBy(article.createdDate.asc());
                break;
            case OrderConst.VIEWS_DESC:
                query.orderBy(article.views.desc(),article.views.desc());
                break;
            case OrderConst.LIKE_COUNT_DESC:
                query.leftJoin(likeArticle)
                        .on(article.id.eq(likeArticle.id))
                        .groupBy(article.id)
                        .orderBy(likeArticle.count().desc(),article.views.desc());
                break;
            case OrderConst.COMMENT_COUNT_DESC:
                query.leftJoin(comment)
                        .on(article.id.eq(comment.article.id))
                        .groupBy(article.id)
                        .orderBy(comment.count().desc(),article.views.desc());
                break;
        }
        List<Article> results = query.fetch();

        boolean hasNext = false;
        if (results.size() > size) {//결과가 6개이면 size(5)보다 크므로 다음 페이지가 있다는 의미
            hasNext = true;
            results.remove(size - 1);//다음 페이지 확인을 위하 게시글을 하나더 가져왔으므로 확인 후 삭제
        }
        return new ArticlePagingResponse<>(results,hasNext);
    }

    //페이징 처리를 하지않은 동적쿼리문 -> 테스트에서 사용

    public List<Article> searchBooleanBuilder(ArticleSearchCond cond) {

        //where문을 보면 ,로 구분이 되었는데 이는 and조건이므로 or로 조건을 걸어야하는 키워드검색은
        //BooleanBuilder 객체를 사용해서 조건들을 체이닝해준다.
        //BooleanBuilder객체를 사용하지 않고 체이닝을 하면 제일 앞에있는 조건의 값이 null일경우 에러가 발생하게된다.
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(contentLike(cond.getContent()))//글 내용 keyword검색
                .or(nickNameLike(cond.getWriter()))//작성자(닉네임) keyword검색
                .or(nameLike(cond.getWriter()))//작성자(이름) keyword검색
                .or(tagArticleIn(cond.getArticlesByTagValue()))//태그 keyword검색
                .or(restaurantNameLike(cond.getRestaurantName()));//음식점명 keyword검색

        return queryFactory.selectFrom(article)
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
        return latitude != null ? article.restaurant.location.latitude.between(latitude - kmToLat, latitude + kmToLat) : null;
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
        return likeArticles != null && !likeArticles.isEmpty() ? article.in(likeArticles) : null;
    }
    //글 내용조건의 keyword로 검색한 게시글들
    private BooleanExpression contentLike(String content) {
        return StringUtils.hasText(content) ? article.content.contains(content) : null;
    }
    //작성자 조건의의 keyword로 검색한 게시글들
    private BooleanExpression nickNameLike(String writer) {
        return StringUtils.hasText(writer) ? article.member.nickName.contains(writer) : null;
    }
    //작성자 조건의의 keyword로 검색한 게시글들
    private BooleanExpression nameLike(String writer) {
        return StringUtils.hasText(writer) ? article.member.name.contains(writer) : null;
    }
    //태그명 조건의 keyword로 검색한 게시글들
    private BooleanExpression tagArticleIn(List<Article> tagArticle) {
        return tagArticle != null && tagArticle.size() > 0 ? article.in(tagArticle) : null;
    }
    //음식점명 조건의의 keyword로 검색한 게시글들
    private BooleanExpression restaurantNameLike(String restaurant) {
        return StringUtils.hasText(restaurant) ? article.restaurant.name.contains(restaurant) : null;
    }
    //상태에 따른 게시글들 필터링
    private BooleanExpression statusEq(ArticleStatus status){
        return status != null ? article.status.eq(status) : null;
    }
    // 무한 스크롤 방식 처리하는 메서드
    // no-offset 방식 처리하는 메서드

    private BooleanExpression ltStoreId(Long articleId) {
        if (articleId == null) {
            return null;
        }
        return article.id.lt(articleId);
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? article.member.id.eq(memberId) : null;
    }

    private BooleanExpression articleIdEq(Long articleId) {
        return articleId != null ? article.id.eq(articleId) : null;
    }

    private BooleanExpression blockMemberIdNotIn(List<Long> blockedMemberIds) {
        return blockedMemberIds != null && !blockedMemberIds.isEmpty() ? article.member.id.notIn(blockedMemberIds) : null;
    }
}
