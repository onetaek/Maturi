package com.maturi.dto.article;

import com.maturi.entity.article.Article;
import com.maturi.entity.article.Location;
import com.maturi.entity.member.Area;
import com.maturi.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class ArticleSearchCond {

    private List<Member> followMembers;//DB에서 가져옴 : 팔로우한 유저의
    private Area area;//DB에서 가져옴 : 관심지역(sido, sigoon, dong)
    private Double latitude;//프론트에서 받음 : 위도 1도 : 111.1412Km
    private Double longitude;//프론트에서 받음 : 경도 1도=111Km+
    private String category;//프론트에서 받음 : 카테고리(한식,중식,...)
    private List<Article> likeArticles;//DB에서 가져옴 : 유저가 좋아요를 누른 게시판

    private String content;//프론트에서 받음 : 글 내용
    private String writer;//프론트에서 받음 : 작성자
    private List<Article> articlesByTagValue;//DB에서 가져옴 : 키워드를 가지고있는 태그명이 참조하는 게시글
    private String restaurantName;//프론트에서 받음 : 레스토랑 이름
}