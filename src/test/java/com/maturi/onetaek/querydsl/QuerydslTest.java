package com.maturi.onetaek.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
  JPAQueryFactory queryFactory = new JPAQueryFactory(em);
  @Test
  void test(){



  }
}
