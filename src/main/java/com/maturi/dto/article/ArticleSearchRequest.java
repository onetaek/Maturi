package com.maturi.dto.article;

import lombok.Data;

@Data
public class ArticleSearchRequest {
    //옵션 검색 조건들
//    private String follow;//팔로우
//    private Area area;//관심지역
//    private Location location;//현재위치
//    private String category;//카테고리
//    private boolean isLike;//좋아요를 선택했다면 유저의 팔로우정보를 가져와야한다.
    /**
     * 1. 전체
     * 2. 팔로우
     * 3. 관심지역
     * 4. 현재위치
     * 5. 카테고리
     * 6. 좋아요
     */
    private String radioCond;


    //키워드 검색 조건
    /**
     * 1. 전체
     * 2. 글 내용
     * 3. 작성자
     * 4. 해시태그
     * 5. 가게명
     */
//    private String content;
//    private String writer;
//    private String tagValue;
//    private String restaurantName;

}
