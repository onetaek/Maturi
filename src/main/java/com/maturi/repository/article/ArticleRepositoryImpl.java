package com.maturi.repository.article;

import com.maturi.dto.article.ArticleSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA와 같이 쓸때 사용하는 구현체
 * <JPA Respository에 적용하는 방법>
 * 1. JpaRepository를 extneds받은 인터페이스의 이름(ArticleRepository)에서 + Impl을 한 class를 생성한다.
 * 2. 원하는 이름의 interface를 생성한다.(여기선 ArticleRepositoryCustom)
 * 3. 인터페이스의 이름(ArticleRepository)에서 + Impl을 한 class에 방금생성한 interface를 상속 받는다.
 * 4. 방금생성한 interface를 JpaRepository를 extneds받은 인터페이스에 상속해준다.
 */
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    @Override
    public Page<ArticleSearchRequest> searchAndPaging(ArticleSearchRequest condition, Pageable pageable) {




        return null;
    }
}
