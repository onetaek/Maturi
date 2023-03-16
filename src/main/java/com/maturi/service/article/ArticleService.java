package com.maturi.service.article;

import com.maturi.dto.article.ArticleDTO;
import com.maturi.dto.member.MemberDTO;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Member;
import com.maturi.repository.ArticleRepository;
import com.maturi.repository.MemberRepository;
import com.maturi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {

    final private ModelMapper modelMapper;
    final private MemberRepository memberRepository;
    final private ArticleRepository articleRepository;
    final private RestaurantRepository restaurantRepository;

    public MemberDTO memberInfo(Long memberId) {
        return modelMapper.map(memberRepository.findById(memberId).orElse(null), MemberDTO.class);
    }


    public Long write(Long memberId, ArticleDTO articleDTO) {

        Member findMember = memberRepository.findById(memberId).orElse(null);

        Location location = Location.builder()
                .oldAddress(articleDTO.getOldAddress())
                .address(articleDTO.getAddress())
                .latitude(articleDTO.getLatitude())
                .longitude(articleDTO.getLongitude())
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name(articleDTO.getRestaurantName())
                .category(Category.FOOD)
                .location(location)
                .build();

        Restaurant findRestaurant = restaurantRepository.save(restaurant);

        Article article = Article.builder()
                .member(findMember)
                .restaurant(findRestaurant)
                .content(articleDTO.getContent())
                .image(articleDTO.getImage())
                .status(ArticleStatus.NORMAL)
                .build();

        return articleRepository.save(article).getId();
    }
}
