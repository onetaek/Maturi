package com.maturi.repository.restaurant;

import com.maturi.entity.resturant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

}
