package com.maturi.querydsl;

import com.maturi.entity.Member;
import com.maturi.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.maturi.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslTest {
  @PersistenceContext
  EntityManager em;
  JPAQueryFactory queryFactory = new JPAQueryFactory(em);
  @Test
  void test(){


    List<Member> fetch = queryFactory
            .select(member)
            .from(member)
            .fetch();
  }
}
