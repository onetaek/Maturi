package com.maturi.entity.like;

import com.maturi.entity.comment.Comment;
import com.maturi.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Comment comment;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
}
