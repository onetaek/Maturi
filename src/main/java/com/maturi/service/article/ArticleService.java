package com.maturi.service.article;

import com.maturi.dto.article.ArticleDTO;
import com.maturi.dto.member.MemberDTO;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Member;
import com.maturi.repository.article.ArticleRepository;
import com.maturi.repository.member.MemberRepository;
import com.maturi.repository.article.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
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
                .category("패스트푸드")
                .location(location)
                .build();

        Restaurant findRestaurant = restaurantRepository.save(restaurant);

        //파일 업로드 로직 필요

        Article article = Article.builder()
                .member(findMember)
                .restaurant(findRestaurant)
                .content(articleDTO.getContent())
                .status(ArticleStatus.NORMAL)
                .build();

        return articleRepository.save(article).getId();
    }

}
