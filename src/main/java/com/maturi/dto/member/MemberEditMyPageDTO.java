package com.maturi.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEditMyPageDTO {
  private String nickName;
  private String name;
  private MultipartFile profileImg;
  private String profile;
  private MultipartFile coverImg;
}
