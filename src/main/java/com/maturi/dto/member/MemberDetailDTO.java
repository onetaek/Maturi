package com.maturi.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailDTO {
  private String email; // 이메일
  private String contact; // 휴대폰 번호
}
