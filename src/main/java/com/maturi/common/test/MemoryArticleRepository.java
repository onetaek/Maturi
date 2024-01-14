package com.maturi.common.test;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryArticleRepository {

    private final Map<Long, TestArticle> store = new HashMap<>();//DB 테이블의 역할
    private long sequence = 0L;//auto increment를 구현하기 위한 변수

    public TestArticle save(TestArticle testArticle) {//저장하는 메서드
        testArticle.setId(++sequence);
        store.put(testArticle.getId(), testArticle);
        return testArticle;
    }

    public TestArticle findById(Long id) {//찾는 메서드
        return store.get(id);//Map<Long, TestArticle>객체여서 id만 넣으면 TestArticle값이 반환된다.
    }
}
