package com.maturi.service.article;

import com.maturi.dto.article.*;
import com.maturi.dto.article.search.ArticlePagingRequest;
import com.maturi.dto.article.search.ArticleSearchCond;
import com.maturi.dto.article.search.ArticleSearchRequest;
import com.maturi.dto.article.search.ArticlePagingResponse;
import com.maturi.dto.member.MemberBlockDTO;
import com.maturi.dto.member.MemberDTO;
import com.maturi.entity.article.*;
import com.maturi.entity.member.Area;
import com.maturi.entity.member.Member;
import com.maturi.repository.article.*;
import com.maturi.repository.article.like.LikeArticleRepository;
import com.maturi.repository.article.restaurant.RestaurantRepository;
import com.maturi.repository.article.tag.TagRepository;
import com.maturi.repository.article.tag.TagValueRepository;
import com.maturi.repository.member.block.BlockQuerydslRepository;
import com.maturi.repository.member.follow.FollowQuerydslRepository;
import com.maturi.repository.member.member.MemberQuerydslRepository;
import com.maturi.repository.member.member.MemberRepository;
import com.maturi.util.FileStore;
import com.maturi.util.constfield.MessageConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.maturi.util.constfield.SearchCondConst.*;
import static com.maturi.util.constfield.SearchEventConst.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final RestaurantRepository restaurantRepository;
    private final TagRepository tagRepository;
    private final TagValueRepository tagValueRepository;
    private final LikeArticleRepository likeArticleRepository;
    private final FollowQuerydslRepository followQRepository;
    private final FileStore fileStore;
    private final ArticleQuerydslRepository articleQRepository;
    private final MemberQuerydslRepository memberQRepository;
    private final BlockQuerydslRepository blockQRepository;
    private final CommentRepository commentRepository;

    public Long write(Long memberId, ArticleDTO articleDTO) throws IOException {

        Member findMember = memberRepository.findById(memberId).orElse(null);

        Location location = Location.builder()
                .oldAddress(articleDTO.getOldAddress())
                .address(articleDTO.getAddress())
                .latitude(articleDTO.getLatitude())
                .longitude(articleDTO.getLongitude())
                .build();
        Area area = Area.builder()
                .sido(articleDTO.getSido())
                .sigoon(articleDTO.getSigoon())
                .dong(articleDTO.getDong())
                .build();
        Restaurant restaurant = Restaurant.builder()
                .name(articleDTO.getName())
                .category(articleDTO.getCategory().split(" > ")[1])
                .location(location)
                .area(area)
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
        log.info("images = " + images);

        Article article = Article.builder()
                .member(findMember)
                .restaurant(findRestaurant)
                .content(articleDTO.getContent())
                .image(images)
                .imageSize(articleDTO.getImageSize())
                .status(ArticleStatus.NORMAL)
                .build();
        log.info("저장하기 직전의 article",article);
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

    public ArticleViewDTO articleInfo(Long articleId, Long memberId) {
//        Article article = articleRepository.findByIdAndStatus(articleId, ArticleStatus.NORMAL);
        Article article = articleQRepository.findByIdAndStatus(articleId);
        if (article == null ) return null;
        ArticleViewDTO articleViewDTO = getArticleViewDTO(article, memberId);//메서드로 분리했습니당
        log.info("articleViewDTO = {}",articleViewDTO);
        return articleViewDTO;
    }

    public ArticleEditViewDTO articleEditInfo(Long articleId){
        Article article = articleQRepository.findByIdAndStatus(articleId);
        if(article == null) return null;
        ArticleEditViewDTO articleEditDTO = getArticleEditDTO(article);
        log.info("articleEditDTO = {}",articleEditDTO);
        return articleEditDTO;
    }

    public RestaurantDTO restaurantByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);

        return RestaurantDTO.builder()
                .name(article.getRestaurant().getName())
                .category(article.getRestaurant().getCategory()) // 수정 필요
                .oldAddress(article.getRestaurant().getLocation().getOldAddress())
                .address(article.getRestaurant().getLocation().getAddress())
                .latitude(article.getRestaurant().getLocation().getLatitude())
                .longitude(article.getRestaurant().getLocation().getLongitude())
                .build();
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
//        Article findArticle = articleRepository.findByIdAndStatus(articleId, ArticleStatus.NORMAL);
        log.info("articleId = {}", articleId);
        Article findArticle = articleQRepository.findByIdAndStatus(articleId);
        log.info("findArticle = {}", findArticle);
        return findArticle != null;
    }

    public String delete(Long memberId, Long articleId) {
        /* 게시글 삭제 */
        String msg = null;

        // 조건 : id + NORMAL or REPORT
        Article findArticle = articleQRepository.findByIdAndStatus(articleId);

        log.info("delete request... findArticle = {}", findArticle);

        if(findArticle == null){
//            msg = "게시글 삭제 실패! 해당 게시물을 찾을 수 없습니다!";
            msg = MessageConst.NOT_FOUND;
        } else if(!findArticle.getMember().getId().equals(memberId)){
//            msg = "게시글 삭제 실패! 게시글 작성자가 아닙니다!";
            msg = MessageConst.NO_PERMISSION;
        } else { // 게시글 삭제 성공
            findArticle.changeStatus(ArticleStatus.DELETE);
            findArticle = articleRepository.save(findArticle);
            msg = MessageConst.SUCCESS_MESSAGE;
        }

        return msg;
    }


    public ArticlePagingResponse articleSearch(ArticleSearchRequest searchRequest,
                                               ArticlePagingRequest pagingRequest,
                                               Long memberId) {

        ArticleSearchCond cond = getSearchCond(searchRequest, memberId);
        log.info("[articleSearch]검색조건을 필터링한 결과 = {}",cond);
        if((pagingRequest.getEvent().equals(click)  || pagingRequest.getEvent().equals(load))
                &&(searchRequest.getRadioCond().equals(follow)&&(cond.getFollowMembers() == null || cond.getFollowMembers().isEmpty()))
                ||searchRequest.getRadioCond().equals(interestArea) && StringUtils.isEmpty(cond.getSido())
                &&StringUtils.isEmpty(cond.getSigoon()) && StringUtils.isEmpty(cond.getDong())
                ||searchRequest.getRadioCond().equals(myLocation) && StringUtils.isEmpty(cond.getLongitude()) && StringUtils.isEmpty(cond.getLatitude())
                ||searchRequest.getRadioCond().equals(restaurantCategory) && StringUtils.isEmpty(cond.getCategory())
                ||searchRequest.getRadioCond().equals(like) && (cond.getLikeArticles() == null || cond.getLikeArticles().isEmpty())){
            log.info("조건에 해당하는 게시글이 없거나 잘못된 조건입니다.");
            return null;//팔로우한 유저의 게시글을 검색했는데 아무값도 없으면 null을 리턴
        }
        ArticlePagingResponse<Article> result = articleQRepository.searchDynamicQueryAndPaging(pagingRequest.getLastArticleId(), cond,pagingRequest.getSize());
        log.info("[articleSearch]페이징 써칭한 결과 result = {}",result);
        List<ArticleViewDTO> articleViewDTOS = new ArrayList<>();
        for (Article article : result.getContent()) {
            log.info("[articleSearch]페이징 써칭한 게시글 하나의 정보 = {}",article);
            ArticleViewDTO articleViewDTO = getArticleViewDTO(article,memberId);
            articleViewDTOS.add(articleViewDTO);
        }
        result.setContent(articleViewDTOS);
        result.setEvent(pagingRequest.getEvent());
        if (articleViewDTOS.size() == 0 ){//검색조건에 맞는 게시글이 하나도 없을 때
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
                List<Member> followMember = memberQRepository.findFollowingsById(memberId);
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
            log.info("[getSearchCond] articlesByTag = {}",articlesByTag);
            searchCond.setArticlesByTagValue(articlesByTag);
            List<Article> articlesByTagValue = searchCond.getArticlesByTagValue();
            for (Article article : articlesByTagValue) {
                log.info("[getSearchCond] article각각의 값 = {}",article);
            }
        }

        List<Long> blockMemberIds = new ArrayList<>();
        List<MemberBlockDTO> blockMembers = blockQRepository.findBlockMembers(memberId);
        for (MemberBlockDTO blockMember : blockMembers) {
            blockMemberIds.add(blockMember.getId());
        }
        searchCond.setBlockedMemberIds(blockMemberIds);

        log.info("searchCond = {}",searchCond);

        return searchCond;
    }

    /**
     * 해당 회원이 작성한 게시글 리스트 & 페이징 처리
     * @param pagingRequest
     * @param memberId (마이페이지 회원 ID)
     * @return ArticlePagingResponse
     */
    public ArticlePagingResponse articlesByMember(ArticlePagingRequest pagingRequest,
                                                  Long memberId) {

        ArticlePagingResponse<Article> result = articleQRepository.findMyReviewArticles(memberId, pagingRequest.getLastArticleId(), pagingRequest.getSize());
        log.info("[articleSearch]페이징 결과 result = {}",result);

        List<ArticleMyPageViewDTO> articleViewDTOS = new ArrayList<>();
        for (Article article : result.getContent()) {
            log.info("[articleSearch]페이징한 게시글 하나의 정보 = {}", article);
            ArticleMyPageViewDTO articleViewDTO = getArticleMyPageViewDTO(article);
            articleViewDTOS.add(articleViewDTO);
        }
        result.setContentForMyPage(articleViewDTOS);
        result.setEvent(pagingRequest.getEvent());
        if (articleViewDTOS.size() == 0 ){ // 게시글이 하나도 없을 때
            result.setLastArticleId(null);
        } else{ // 게시글 존재 -> 마지막 게시글 ID 저장
            result.setLastArticleId(articleViewDTOS.get(articleViewDTOS.size()-1).getId());
        }
        return result;
    }


    /**
     * article Entity를 DTO로 변환
     * @param article
     * @param memberId (로그인 회원 ID)
     * @return ArticleViewDTO
     */
    private ArticleViewDTO getArticleViewDTO(Article article, Long memberId) {
        if(article == null){
            return null;
        }

        List<TagValue> tagValues = tagValueRepository.findByArticleId(article.getId());
        ArrayList<String> tagName = new ArrayList<>();
        for(TagValue tagValue : tagValues){
            tagName.add("#" + tagValue.getTag().getName());
        }

        int likeNum = likeArticleRepository.countByArticleId(article.getId());
        Long articleMemberId = article.getMember().getId();
        boolean isFollowingMember = followQRepository.isFollowingMember(memberId, articleMemberId);
        log.info("isFollowingMember = {}",isFollowingMember);
        return ArticleViewDTO.builder()
                .id(article.getId())
                .content(article.getContent())
                .image(Arrays.asList(article.getImage().split(",")))
                .modifiedDate(article.getModifiedDate())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .nickName(article.getMember().getNickName())
                .profileImg(article.getMember().getProfileImg())
                .tags(tagName)
                .like(likeNum)
                .isLiked(this.isLikedArticle(article.getId(), memberId))
                .isFollowingMember(isFollowingMember)
                .build();
    }

    private ArticleMyPageViewDTO getArticleMyPageViewDTO(Article article){
        if(article == null) return null;
        ArticleMyPageViewDTO articleDTO = ArticleMyPageViewDTO.builder()
                .id(article.getId())
                .image(Arrays.asList(article.getImage().split(",")).get(0))
                .likeCount(likeArticleRepository.countByArticleId(article.getId()))
                .commentCount(commentRepository.countByArticleId(article.getId()))
                .restaurantName(article.getRestaurant().getName())
                .build();
        Area area = article.getRestaurant().getArea();
        if(area != null){
            articleDTO.setSido(area.getSido());
            articleDTO.setSigoon(area.getSigoon());
            articleDTO.setDong(area.getDong());
        }
        return articleDTO;
    }

    private ArticleEditViewDTO getArticleEditDTO(Article article) {
        if(article == null){
            return null;
        }
        List<TagValue> tagValues = tagValueRepository.findByArticleId(article.getId());
        ArrayList<String> tagName = new ArrayList<>();
        for(TagValue tagValue : tagValues){
            tagName.add("#" + tagValue.getTag().getName());
        }
        return ArticleEditViewDTO.builder()
                .id(article.getId())
                .memberId(article.getMember().getId())
                .content(article.getContent())
                .restaurantName(article.getRestaurant().getName())
                .category(article.getRestaurant().getCategory())
                .sido(article.getRestaurant().getArea().getSido())
                .sigoon(article.getRestaurant().getArea().getSigoon())
                .dong(article.getRestaurant().getArea().getDong())
                .image(Arrays.asList(article.getImage().split(",")))
                .imageSize(article.getImageSize())
                .tags(tagName)
                .build();
    }

    public ArticleViewDTO edit(Long memberId, Long articleId, ArticleEditDTO articleEditDTO) throws IOException {
        // db에 저장된 article 찾기
        Article findArticle = articleQRepository.findByIdAndStatus(articleId);

        /* 태그 */
        // 기존 태그들 삭제
        List<TagValue> tagValues = tagValueRepository.findByArticleId(articleId);
        // 해당 게시글의 모든 태그 삭제
        tagValueRepository.deleteAll(tagValues);//번크 연산으로 한번에 삭제

        // 해당 게시글의 태그들 다시 추가하기
        String[] tags = articleEditDTO.getTags().split("#");

        for(String tagName : tags){
            if(!tagName.equals("") && tagName != null){
                Tag findTag = tagRepository.findByName(tagName);
                if(findTag == null){ // TagName이 db에 저장되어있지 않는 경우
                    Tag tag = Tag.builder()
                            .name(tagName)
                            .build();
                    findTag = tagRepository.save(tag); // db에 해당 tagName 추가!!
                }
                TagValue tagValue = TagValue.builder()
                        .tag(findTag)
                        .article(findArticle)
                        .build();

                tagValueRepository.save(tagValue); // 해당 게시글의 태그들 생성!!
            }
        }

        // 변경된 content 업데이트
        findArticle.changeContent(articleEditDTO.getContent());

        /* 기존의 이미지 추가로직 */
        log.info("images={},",articleEditDTO.getImage());
        //파일 업로드 로직 필요
        List<String> storeImageFiles = fileStore.storeFiles(articleEditDTO.getImage());
        log.info("storeImageFiles = {}", storeImageFiles);
        log.info("index = " + storeImageFiles.size());
        String images = "";
        for(String img : storeImageFiles){
            images += (img + ",");
        }
        log.info("images = " + images);
        /* 수정게시글에서 추가된 로직 */
        String oldImage = articleEditDTO.getOldImage();
        oldImage += images;

        findArticle.changeImage(oldImage);//이전 이미지 + 새롭개 추가된 이미지
        findArticle.changeImageSize(articleEditDTO.getImageSize());

        Article article = articleRepository.save(findArticle); // db에 업데이트

        ArticleViewDTO articleViewDTO = getArticleViewDTO(article, memberId);
        return articleViewDTO;
    }
}
