package com.maturi.entity.article;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
  private String oldAddress;
  private String address;
  private Double latitude;
  private Double longitude;
}
