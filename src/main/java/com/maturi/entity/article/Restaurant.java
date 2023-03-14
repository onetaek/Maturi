package com.maturi.entity.article;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Enumerated(EnumType.STRING)
  private Category category;
  @Embedded//Location에 @Embeddable이 있으면 생략가능함 -> 쓰는걸 권장
  private Location location;
}
