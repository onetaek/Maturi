package com.maturi.service.article;

import com.maturi.dto.article.ArticleDTO;
import com.maturi.dto.article.ArticleViewDTO;
import com.maturi.dto.article.RestaurantDTO;
import com.maturi.dto.member.MemberDTO;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Member;
import com.maturi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    final private ModelMapper modelMapper;
    final private MemberRepository memberRepository;
    final private ArticleRepository articleRepository;
    final private RestaurantRepository restaurantRepository;
    final private TagRepository tagRepository;
    final private TagValueRepository tagValueRepository;
    final private LikeArticleRepository likeArticleRepository;

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
                .name(articleDTO.getName())
                .category(articleDTO.getCategory())
                .location(location)
                .build();

        Restaurant findRestaurant = restaurantRepository.save(restaurant);

        //파일 업로드 로직 필요

        Article article = Article.builder()
                .member(findMember)
                .restaurant(findRestaurant)
                .content(articleDTO.getContent())
                .image(articleDTO.getImage())
                .status(ArticleStatus.NORMAL)
                .build();

        Article findArticle = articleRepository.save(article);

        String[] tags = articleDTO.getTags().split("#");
        for(String tagName : tags){
            if(!tagName.equals("") || tagName != null){
                Tag findTag = tagRepository.findByName(tagName);
                if(findTag == null){
                    Tag tag = Tag.builder()
                            .name(tagName)
                            .build();
                    findTag = tagRepository.save(tag);
                }
                TagValue tagValue = TagValue.builder()
                        .tag(findTag)
                        .article(findArticle)
                        .build();

                tagValueRepository.save(tagValue);
            }
        }

        return findArticle.getId();
    }

    public ArticleViewDTO articleInfo(Long articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);

        List<TagValue> tagValues = tagValueRepository.findByArticleId(article.getId());
        ArrayList<String> tagName = new ArrayList<>();
        for(TagValue tagValue : tagValues){
            tagName.add("#" + tagValue.getTag().getName());
        }

        int likeNum = likeArticleRepository.countByArticleId(article.getId());

        ArticleViewDTO articleViewDTO = ArticleViewDTO.builder()
                .id(articleId)
                .content(article.getContent())
//                .image(null)
                .modifiedDate(article.getModifiedDate())
                .name(article.getMember().getName())
                .profileImg(article.getMember().getProfileImg())
                .tags(tagName)
                .like(likeNum)
//                .restaurantName(article.getRestaurant().getName())
//                .category(article.getRestaurant().getCategory()) // 수정??
//                .oldAddress(article.getRestaurant().getLocation().getOldAddress())
//                .address(article.getRestaurant().getLocation().getAddress())
//                .latitude(article.getRestaurant().getLocation().getLatitude())
//                .longitude(article.getRestaurant().getLocation().getLongitude())
                .build();
        return articleViewDTO;
    }

    public RestaurantDTO restaurantByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);

        RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                .name(article.getRestaurant().getName())
                .category(article.getRestaurant().getCategory()) // 수정 필요
                .oldAddress(article.getRestaurant().getLocation().getOldAddress())
                .address(article.getRestaurant().getLocation().getAddress())
                .latitude(article.getRestaurant().getLocation().getLatitude())
                .longitude(article.getRestaurant().getLocation().getLongitude())
                .build();
        return restaurantDTO;
    }

    public boolean isLikedArticle(Long articleId, Long memberId){
        LikeArticle likeArticle = likeArticleRepository.findByArticleIdAndMemberId(articleId, memberId);
        return likeArticle != null;
    }

    public int likeOrUnlike(Long memberId, Long articleId) {
        LikeArticle findLikeArticle = likeArticleRepository.findByArticleIdAndMemberId(articleId, memberId);

        if(findLikeArticle == null){ // 좋아요 안한 상태
            LikeArticle likeArticle = LikeArticle.builder()
                    .article(articleRepository.findById(articleId).orElse(null))
                    .member(memberRepository.findById(memberId).orElse(null))
                    .build();
            findLikeArticle = likeArticleRepository.save(likeArticle);
        } else { // 이미 좋아요 한 상태
            likeArticleRepository.delete(findLikeArticle);
        }

        int likeNum = likeArticleRepository.countByArticleId(articleId);

        return likeNum;
    }
}
