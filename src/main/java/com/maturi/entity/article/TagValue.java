package com.maturi.entity.article;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Tag tag;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id")
  private Article article;
}
