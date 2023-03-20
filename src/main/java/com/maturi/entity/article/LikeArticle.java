package com.maturi.entity.article;

import com.maturi.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import javax.xml.ws.BindingType;

@Getter
@ToString
@Builder
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
