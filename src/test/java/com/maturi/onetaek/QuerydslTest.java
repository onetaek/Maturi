package com.maturi.onetaek;

import com.maturi.entity.QTag;
import com.maturi.entity.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
@SpringBootTest
@Transactional
public class QuerydslTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("querydsl 작동하는지 확인")
    void querydslTest(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Tag tag = new Tag(null,"tag1");
        em.persist(tag);

        Tag tag1 = queryFactory
                .selectFrom(QTag.tag)
                .fetchOne();
        Assertions.assertThat(tag1.getName()).isEqualTo("tag1");
    }
}
