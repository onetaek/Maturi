package com.maturi.repository.restaurant;

import com.maturi.entity.article.ArticleStatus;
import com.maturi.entity.article.QRestaurant;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.maturi.entity.article.QArticle.article;
import static com.maturi.entity.article.QRestaurant.*;

@RequiredArgsConstructor
@Repository
public class RestaurantQuerydslRepository {

    private final JPAQueryFactory query;

    public List<String> getCategory(){
        return query.select(restaurant.category)
                .from(article)
                .join(article.restaurant,restaurant)
                .on(article.status.eq(ArticleStatus.NORMAL))
                .groupBy(restaurant.category)
                .fetch();
    }
}
