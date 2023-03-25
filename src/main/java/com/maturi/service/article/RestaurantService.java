package com.maturi.service.article;

import com.maturi.entity.article.Restaurant;
import com.maturi.repository.article.restaurant.RestaurantQuerydslRepository;
import com.maturi.repository.article.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantQuerydslRepository restaurantQRepository;


    public List<String> getCategory(){
        return restaurantQRepository.getCategory();
    }
}
