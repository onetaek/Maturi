package com.maturi.dto.article;

import lombok.Data;

@Data
public class ArticleSearchRequest {

    /**
     * 옵션 검색 조건들
     * 1. 전체
     * 2. 팔로우
     * 3. 관심지역
     * 4. 현재위치
     * 5. 카테고리
     * 6. 좋아요
     */
    private String radioCond;//이걸 통해서 radtio checked 처리함

    //전체를 눌렀을 때는 where문에 싹다 null로 들어가야한다.

    //팔로우, 좋아요, 관심지역를 눌렀을 떄는 session에 있는 user의 정보를 기반으로 가져와야해서 value가 필요없다.

    //현재위치를 눌렀을 때
    private String latitude;//내위도
    private String longitude;//내경도

    //카테고리를 눌렀을 때
    private String category;//카테고리

    /**
     * 키워드 검색 조건
     * 1. 전체
     * 2. 글 내용
     * 3. 작성자
     * 4. 해시태그
     * 5. 가게명
     */
    private String content;
    private String writer;
    private String tagValue;
    private String restaurantName;


}
