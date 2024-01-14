package com.maturi.entity.resturant;

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
