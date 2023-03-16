package com.maturi.entity.article;

import com.maturi.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LikeArticle {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
}
