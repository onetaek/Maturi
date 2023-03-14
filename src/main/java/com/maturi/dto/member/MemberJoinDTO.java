package com.maturi.dto.member;

import com.maturi.entity.member.MemberStatus;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
