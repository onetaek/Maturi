package com.maturi.test;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryArticleRepository {

    private final Map<Long, TestArticle> store = new HashMap<>();
    private long sequence = 0L;

    public TestArticle save(TestArticle testArticle) {
        testArticle.setId(++sequence);
        store.put(testArticle.getId(), testArticle);
        return testArticle;
    }

    public TestArticle findById(Long id) {
        return store.get(id);
    }
}
