package com.maturi.service.article;

import com.maturi.dto.article.ArticleDTO;
import com.maturi.dto.article.ArticleViewDTO;
import com.maturi.dto.article.RestaurantDTO;
import com.maturi.dto.member.MemberDTO;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Member;
import com.maturi.repository.article.*;
import com.maturi.repository.member.MemberRepository;
import com.maturi.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final CommentRepository commentRepository;

    final private ModelMapper modelMapper;
    final private MemberRepository memberRepository;
    final private ArticleRepository articleRepository;
    final private RestaurantRepository restaurantRepository;
    final private TagRepository tagRepository;
    final private TagValueRepository tagValueRepository;
    final private LikeArticleRepository likeArticleRepository;
    final private FileStore fileStore;

    public MemberDTO memberInfo(Long memberId) {
        return modelMapper.map(memberRepository.findById(memberId).orElse(null), MemberDTO.class);
    }

    public Long write(Long memberId, ArticleDTO articleDTO) throws IOException {

        Member findMember = memberRepository.findById(memberId).orElse(null);

        Location location = Location.builder()
                .oldAddress(articleDTO.getOldAddress())
                .address(articleDTO.getAddress())
                .latitude(articleDTO.getLatitude())
                .longitude(articleDTO.getLongitude())
                .build();
        Restaurant restaurant = Restaurant.builder()
                .name(articleDTO.getName())
                .category(articleDTO.getCategory().split(" > ")[1])
                .location(location)
                .build();

        Restaurant findRestaurant = restaurantRepository.save(restaurant);

        log.info("images={},",articleDTO.getImage());
        //파일 업로드 로직 필요
        List<String> storeImageFiles = fileStore.storeFiles(articleDTO.getImage());
        log.info("storeImageFiles = {}", storeImageFiles);
        log.info("index = " + storeImageFiles.size());
        String images = "";
        for(String img : storeImageFiles){
            images += (img + ",");
        }
        log.info("image = " + images);

        Article article = Article.builder()
                .member(findMember)
                .restaurant(findRestaurant)
                .content(articleDTO.getContent())
                .image(images)
                .status(ArticleStatus.NORMAL)
                .build();

        Article findArticle = articleRepository.save(article);

        String[] tags = articleDTO.getTags().split("#");
        for(String tagName : tags){
            if(!tagName.equals("") && tagName != null){
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
        Article article = articleRepository.findByIdAndStatus(articleId, ArticleStatus.NORMAL);

        if(article == null){
            return null;
        }

        List<TagValue> tagValues = tagValueRepository.findByArticleId(article.getId());
        ArrayList<String> tagName = new ArrayList<>();
        for(TagValue tagValue : tagValues){
            tagName.add("#" + tagValue.getTag().getName());
        }

        int likeNum = likeArticleRepository.countByArticleId(article.getId());

        ArticleViewDTO articleViewDTO = ArticleViewDTO.builder()
                .id(articleId)
                .content(article.getContent())
                .image(Arrays.asList(article.getImage().split(",")))
                .modifiedDate(article.getModifiedDate())
                .name(article.getMember().getName())
                .profileImg(article.getMember().getProfileImg())
                .tags(tagName)
                .like(likeNum)
                .build();
        return articleViewDTO;
    }

    public RestaurantDTO restaurantByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);

        if(article == null){
            return null;
        }

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

    public boolean likeOrUnlike(Long memberId, Long articleId) {
        LikeArticle findLikeArticle = likeArticleRepository.findByArticleIdAndMemberId(articleId, memberId);

        if(findLikeArticle == null){ // 좋아요 안한 상태
            LikeArticle likeArticle = LikeArticle.builder()
                    .article(articleRepository.findById(articleId).orElse(null))
                    .member(memberRepository.findById(memberId).orElse(null))
                    .build();
            findLikeArticle = likeArticleRepository.save(likeArticle);
            return true;
        } else { // 이미 좋아요 한 상태
            likeArticleRepository.delete(findLikeArticle);
            return false;
        }
    }
    public int likeNum(Long articleId){
        return likeArticleRepository.countByArticleId(articleId);
    }

    public boolean articleStatusNormal(Long articleId) {
        Article findArticle = articleRepository.findByIdAndStatus(articleId, ArticleStatus.NORMAL);
        log.info("findArticle = {}", findArticle);
        return findArticle != null;
    }

    public String delete(Long memberId, Long articleId) {
        /* 게시글 삭제 */
        String msg = null;

        // 조건 : id + NORMAL
        Article findArticle = articleRepository.findByIdAndStatus(articleId, ArticleStatus.NORMAL);

        if(findArticle == null){
            msg = "게시글 삭제 실패! 해당 게시물을 찾을 수 없습니다!";
        } else if(!Objects.equals(findArticle.getMember().getId(), memberId)){
            msg = "게시글 삭제 실패! 게시글 작성자가 아닙니다!";
        } else { // 게시글 삭제 성공
            findArticle.changeStatus(ArticleStatus.DELETE);
            articleRepository.save(findArticle);
            msg = "게시글을 삭제하였습니다.";
        }

        return msg;
    }
}
