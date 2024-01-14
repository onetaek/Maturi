package com.maturi.dto.article.search;

import com.maturi.entity.article.Article;
import com.maturi.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleSearchCond {

    private List<Long> blockedMemberIds;
    private List<Member> followMembers;//DB에서 가져옴 : 팔로우한 유저의
    private String sido;//DB에서 가져옴 : 관심지역(sido)
    private String sigoon;//DB에서 가져옴 : 관심지역(sigoon)
    private String dong;//DB에서 가져옴 : 관심지역(dong)
    private Double latitude;//프론트에서 받음 : 위도 1도 : 111.1412Km
    private Double longitude;//프론트에서 받음 : 경도 1도=111Km+
    private String category;//프론트에서 받음 : 카테고리(한식,중식,...)
    private List<Article> likeArticles;//DB에서 가져옴 : 유저가 좋아요를 누른 게시판

    private String content;//프론트에서 받음 : 글 내용
    private String writer;//프론트에서 받음 : 작성자
    private List<Article> articlesByTagValue;//DB에서 가져옴 : 키워드를 가지고있는 태그명이 참조하는 게시글
    private String restaurantName;//프론트에서 받음 : 레스토랑 이름
}
