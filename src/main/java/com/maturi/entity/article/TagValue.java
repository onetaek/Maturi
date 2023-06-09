package com.maturi.entity.article;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = "article")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Tag tag;
  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;
}
