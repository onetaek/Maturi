package com.maturi.entity.article;

import com.maturi.entity.BaseTimeEntity;
import com.maturi.entity.member.Member;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //대댓글 구현하기위한 변수
  private Long ref;//댓글의 덩어리
  private Long refStep;//댓글의 깊이
  private Long refMemberId;//댓글의 깊이가 3일때 사용할 변수
  private String refMemberNickName;//유튜브의 @닉네임을 표현하기위한 변수

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;
  @Column(columnDefinition = "TEXT")
  private String content;
  @Enumerated(EnumType.STRING)
  private CommentStatus status;



  public void changeStatus(CommentStatus status){
    this.status = status;
  }
  public void changeContent(String content){
    this.content = content;
  }
}
