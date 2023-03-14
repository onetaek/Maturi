package com.maturi.dto.member;

import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class MemberJoinDTO {
  private String email;
  private String passwd;
  private String salt;
  private String name;
  private String nickName;

  private MemberStatus status;

  public Member toEntity(){
    return new Member(email, passwd, salt, name, nickName, status);
  }

}
