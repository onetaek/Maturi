package com.maturi.entity.member;

import com.maturi.dto.member.AreaInterDTO;
import lombok.*;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@Getter
@Embeddable
public class Area {
  private String sido;
  private String sigoon;
  private String dong;
}
