package com.maturi.api.article;

import com.maturi.service.article.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestaurantAPIController {

    final private RestaurantService restaurantService;

    @GetMapping("/restaurants/category")
    public ResponseEntity<List<String>> getRestaurantCategory(){
        log.info("restaurant category찾기");
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getCategory());
    }
}
