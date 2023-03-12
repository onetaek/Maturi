package com.maturi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY) // 필요한 경우에만 해당 테이블 join함
  private Member member;
  @ManyToOne(fetch = FetchType.LAZY)
  private Restaurant restaurant;

  private String content;
  private String image; // 이미지 여러개 업로드 가능
  private LocalDate writtenAt;
  private LocalDate updatedAt;
  private String status;
}
