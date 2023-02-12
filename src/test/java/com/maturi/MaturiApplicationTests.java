package com.maturi;

import com.maturi.entity.QTag;
import com.maturi.entity.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
class MaturiApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads(){
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		Tag tag = new Tag(null,"tag1");
		em.persist(tag);

		Tag tag1 = queryFactory
				.selectFrom(QTag.tag)
				.fetchOne();
		Assertions.assertThat(tag1.getName()).isEqualTo("tag1");

	}


}
