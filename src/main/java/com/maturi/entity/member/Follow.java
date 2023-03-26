package com.maturi.entity.member;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Follow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member followerMember;//나를 팔로우하는 사람
  @ManyToOne(fetch = FetchType.LAZY)
  private Member followingMember;//내가 팔로우하는 사람

  //내가 1일때 3번 유저를 팔로우 하려면
  //followerMember = 1, followingMember = 3;
  //3번 유저의 입장에선 팔로워가 1번이다.

}
