package com.maturi.entity.article;

import com.maturi.entity.member.Area;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String category;
  @Embedded//Location에 @Embeddable이 있으면 생략가능함 -> 쓰는걸 권장
  private Location location;
  @Embedded//Area에 @Embeddable이 있으면 생략가능함 -> 쓰는걸 권장
  private Area area;
}
