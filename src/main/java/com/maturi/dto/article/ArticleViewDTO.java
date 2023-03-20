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
  private String image; // 아직 작업x
  private LocalDateTime modifiedDate; // 마지막 업데이트 날짜
  // 글 작성자 정보
  private String name;
  private String profileImg;
  // 태그 리스트
  private List<String> tags;
  // 좋아요 관련
  private int like;
//  // 음식점 정보
//  private String restaurantName;
//  private Category category;
//  private String oldAddress;
//  private String address;
//  private Double latitude;
//  private Double longitude;
}
