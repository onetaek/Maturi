package com.maturi.service.article;

import com.maturi.dto.article.*;
import com.maturi.dto.article.search.ArticleSearchCond;
import com.maturi.dto.article.search.ArticleSearchRequest;
import com.maturi.dto.article.search.ArticlePaging;
import com.maturi.dto.member.MemberDTO;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Area;
import com.maturi.entity.member.Member;
import com.maturi.repository.article.*;
import com.maturi.repository.member.MemberRepository;
import com.maturi.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.maturi.util.constfield.SearchCondConst.*;

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
    final private ArticleQuerydslRepository articleQRepository;
    final private MemberQuerydslRepository memberQRepository;

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
        if (article == null ) return null;
        ArticleViewDTO articleViewDTO = getArticleViewDTO(article);//메서드로 분리했습니당
        log.info("articleViewDTO = {}",articleViewDTO);
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


    public ArticlePaging articleSearch(ArticleSearchRequest searchRequest,
                                       Long memberId,
                                       Long lastArticleId,
                                       Pageable pageable) {
        log.info("size? = {}",pageable.getPageSize());
        ArticleSearchCond cond = getSearchCond(searchRequest, memberId);
        ArticlePaging result = articleQRepository.searchDynamicQueryAndPaging(lastArticleId, cond, pageable);//DTO로 변환해야함
        log.info("Slice<Article> articles = {}",result);

        List<ArticleViewDTO> articleViewDTOS = new ArrayList<>();
        for (Article article : (List<Article>)result.getContent()) {
            ArticleViewDTO articleViewDTO = getArticleViewDTO(article);
            log.info("[ArticleService] articleViewDTO = {}",articleViewDTO);
            articleViewDTOS.add(articleViewDTO);
        }
        result.setContent(articleViewDTOS);
        result.setEvent(searchRequest.getEvent());
        if (articleViewDTOS.size() == 0 ){
            result.setLastArticleId(null);
        } else{
            result.setLastArticleId(articleViewDTOS.get(articleViewDTOS.size()-1).getId());
        }
        return result;
    }


    /**
     * 클라이언트로 부터 받은 정보를 기반으로 검색조건에 데이터로 변환해줌(ArticleSearchRequest -> ArticleSearchCond)
     */
    public ArticleSearchCond getSearchCond(ArticleSearchRequest searchRequest, Long memberId) {
        ArticleSearchCond searchCond = modelMapper.map(searchRequest, ArticleSearchCond.class);
        switch (searchRequest.getRadioCond() != null ? searchRequest.getRadioCond() : NULL){
            case follow://유저가 팔로우한 유저들
                List<Member> followMember = memberQRepository.findFollowMemberById(memberId);
                searchCond.setFollowMembers(followMember);
                break;
            case interestArea://유저의 관심 지역
                Area interArea = memberRepository.findById(memberId).orElseThrow(() ->
                        new IllegalArgumentException("맴버가 없습니다!")).getArea();
                if (interArea == null ){
                    searchCond.setSido(null);
                    searchCond.setSigoon(null);
                    searchCond.setDong(null);
                } else {
                    searchCond.setSido(interArea.getSido());
                    searchCond.setSigoon(interArea.getSigoon());
                    searchCond.setDong(interArea.getDong());
                }
                break;
            case like://유저가 좋아요를 누른 게시판들
                List<Article> likeArticles =    articleQRepository.findByLike(memberId);
                searchCond.setLikeArticles(likeArticles);
                break;
        }
        if (StringUtils.hasText(searchRequest.getAll())){//keyword검색의 dropdown메뉴중 전체를 선택했을 때
            String keyword = searchRequest.getKeyword();
            searchCond.setContent(keyword);
            searchCond.setWriter(keyword);
            searchCond.setRestaurantName(keyword);
        }
        if (StringUtils.hasText(searchRequest.getTag())) {//keyword검색의 dropdown메뉴중 태그를 선택했을 때
            List<Article> articlesByTag = articleQRepository.findByTagValue(searchRequest.getTag().trim());
            log.info("[ArticleService] articlesByTag = {}",articlesByTag);
            searchCond.setArticlesByTagValue(articlesByTag);
        }
        return searchCond;
    }

    //article Entity를 DTO로 변환
    private ArticleViewDTO getArticleViewDTO(Article article) {
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
                .id(article.getId())
                .content(article.getContent())
                .image(Arrays.asList(article.getImage().split(",")))
                .modifiedDate(article.getModifiedDate())
                .name(article.getMember().getName())
                .nickName(article.getMember().getNickName())
                .profileImg(article.getMember().getProfileImg())
                .tags(tagName)
                .like(likeNum)
                .isLiked(this.isLikedArticle(article.getId(), article.getMember().getId()))
//                .restaurantName(article.getRestaurant().getName())
//                .category(article.getRestaurant().getCategory()) // 수정??
//                .oldAddress(article.getRestaurant().getLocation().getOldAddress())
//                .address(article.getRestaurant().getLocation().getAddress())
//                .latitude(article.getRestaurant().getLocation().getLatitude())
//                .longitude(article.getRestaurant().getLocation().getLongitude())
                .build();
        return articleViewDTO;
    }
}
