package com.maturi.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleViewDTO {
  // 기본 article 정보
  private Long id;
  private String content;
  private List<String> image;
  private String date;//포매팅한 날짜

  // 글 작성자 정보
  private Long memberId;
  private String name;
  private String nickName;
  private String profileImg;

  //음식점 정보
  private String address;
  private String restaurantName;

  // 태그 리스트
  private List<String> tags;
  // 좋아요 관련
  private int like;
  private boolean isLiked;
  //댓글 갯수
  private int commentCount;
  //조회수
  private int views;
  //팔로잉 유저인지 확인하기위함
  private boolean isFollowingMember;

  //추가 및 수정 된것들
  //1. date
  //2. commentCount
  //3. views
}
