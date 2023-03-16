package com.maturi.entity.article;

import com.maturi.entity.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY) // 필요한 경우에만 해당 테이블 join함
  private Member member;
  @ManyToOne(fetch = FetchType.LAZY)
  private Restaurant restaurant;
  @Column(length = 10000)
  private String content;
  @Column(length = 10000)
  private String image; // 이미지 여러개 업로드 가능
  @CreatedDate
  private LocalDate writtenAt;
  @LastModifiedDate
  private LocalDate updatedAt;
  @Enumerated(EnumType.STRING)
  private ArticleStatus status;
}
