package com.maturi.repository.article;

import com.maturi.dto.article.ArticleSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom{
    Page<ArticleSearchRequest> searchAndPaging(ArticleSearchRequest condition, Pageable pageable);
}
