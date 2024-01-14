package com.maturi.entity.like;

import com.maturi.entity.article.Article;
import com.maturi.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import javax.xml.ws.BindingType;

@Getter
@ToString
@AllArgsConstructor
@Builder
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
