package com.maturi.querydsl;

import com.maturi.entity.article.Article;
import com.maturi.entity.article.QArticle;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Supplier;

import static com.maturi.entity.article.QArticle.*;

@Slf4j
@Transactional
public class DynamicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory query;

    @BeforeEach
    public void before(){
        query = new JPAQueryFactory(em);
    }

    @Test
    void eq에Null을넣을때(){

        String content = "내용";

        List<Article> findArticles =
                query.selectFrom(article)
                .fetch();
        log.info("findArticles = {}",findArticles);

    }

    private BooleanBuilder nameEq(String content){
        return nullSafeBuilder(()-> article.content.eq(content));
    }

    public static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }
}
