package com.maturi.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    private String name;
    private String category;
    private String oldAddress;
    private String address;
    private Double latitude;
    private Double longitude;
    private String sido;
    private String sigoon;
    private String dong;
}


