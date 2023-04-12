package com.maturi.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

  private Long id;
  private Long ref;//댓글의 묶음을 표현하기 위한 변수
  private Long refStep;//댓글의 깊이
  private String profileImg;
  private Long memberId;
  private String name;
  private String nickName;
  private String content;
  private String duration;//작성날짜로 부터 몇일이 지났는지
  private boolean isModified;//수정 유무를 확인
  private int likeCount;//좋아요 갯수
  private boolean isLiked;

  //댓글의 깊이(refStep)가 3일 때만 사용하는 변수
  private Long refMemberId;
  private String refMemberNickName;

}
