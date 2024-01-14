package com.maturi.entity.tag;

import com.maturi.entity.article.Article;
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
