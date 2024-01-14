package com.maturi.service.article;

import com.maturi.repository.restaurant.RestaurantQuerydslRepository;
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
