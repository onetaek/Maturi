package com.maturi.entity.article;

import lombok.*;

import javax.persistence.Embeddable;
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {
  private String oldAddress;
  private String address;
  private Double latitude;
  private Double longitude;
}
