package com.maturi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;

  private String content;
  private LocalDate writtenAt;
  private LocalDate updateAt;
  private String status;
}
