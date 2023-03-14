package com.maturi.dto.member;

import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberJoinDTO {
  private String email;
  private String passwd;
  private String salt;
  private String name;
  private String nickName;
  private MemberStatus status;
}
