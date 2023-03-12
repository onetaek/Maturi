package com.maturi.onetaek.querydsl;

import com.maturi.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;


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
