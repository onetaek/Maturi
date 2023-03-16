package com.maturi.entity.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
@Builder
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
