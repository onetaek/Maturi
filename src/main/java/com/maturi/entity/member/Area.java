package com.maturi.entity.member;

import com.maturi.dto.member.AreaInterDTO;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Area {
  private String sido;
  private String sigoon;
  private String dong;
}
