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
public class MemberMyPageDTO {
  private String nickName;
  private String name;
  private String profileImg;
  private String profile;
  private String coverImg;
}
