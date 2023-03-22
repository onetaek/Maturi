package com.maturi.util.dummy;


import com.maturi.entity.article.*;
import com.maturi.entity.member.Area;
import com.maturi.entity.member.Follow;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.repository.article.ArticleRepository;
import com.maturi.repository.article.RestaurantRepository;
import com.maturi.repository.article.TagRepository;
import com.maturi.repository.article.TagValueRepository;
import com.maturi.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 랜덤 더미 데이터 생성
 * 1. Article(Entity) - Level 5 !!
 * 2. Comment(Entity) - Level 4 pass
 * 3. LikeArticle(Entity) - Level 5 일단 pass
 * 4. LikeComment(Entity) - Level 5 일단 pass
 * 5. Location - Level 1 !!
 * 6. Restaurant(Entity) - Level 2 !!
 * 7. Tag(Entity) - Level 1 !!
 * 8. TagValue(Entity) - Level 6
 *
 * 9. Area - Level 1 !!
 * 10. Follow(Entity) - Level 3 !!
 * 11. Member(Entity) - Level 2 !!
 * 12. MemberStatus - Level 1 !(무조건 NORMAL)!
 */
@Component
@RequiredArgsConstructor
public class DummyData {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initTag();
        initService.initRestaurant();
        initService.initMember();
        initService.initFollow();
        initService.initArticle();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        @Autowired
        private final EntityManager em;
        private final MemberRepository memberRepository;
        private final RestaurantRepository restaurantRepository;
        private final TagValueRepository tagValueRepository;
        private final TagRepository tagRepository;
        private final ArticleRepository articleRepository;
        Double latitude = 35.8700317;
        Double longitude = 128.6005225;
        String sido = "대구광역시";
        String[] sigoon = {"중구","동구","서구","북구","수성구","달서구","달성군",};
        //        MemberStatus[] memberStatuses = {MemberStatus.BAN}
        String[] category = {"한식","중식","일식","디져트","패스트푸드"};
        String[] email = {"naver","gmail","nate"};
        private Integer random(int count){
            return (int)(Math.random()*count);
        }


        public void initTag() {
            for(int i = 0; i < 20 ; i ++){
                Tag tag = Tag.builder()
                        .name("태그"+random(1000000).toString())
                        .build();
                em.persist(tag);
            }
        }

        public void initRestaurant(){
            for(int i = 0 ; i < 20 ; i ++){
                Restaurant restaurant = createRestaurant();
                em.persist(restaurant);
            }
        }

        public void initMember(){
            for(int i = 0 ; i < 20 ; i++){
                Member member =createMember();
                em.persist(member);
            }
        }

        public void initFollow(){
            for(int i = 0; i < 20 ; i++){
                List<Member> findMembers = memberRepository.findAll();
                int random1 = random(findMembers.size()-1);
                int random2 = random(findMembers.size()-1);
                while (random1 >= random2){
                    random2 = random(findMembers.size());
                }
                Member member1 = findMembers.get(random1);
                Member member2 = findMembers.get(random2);
                Follow follow = Follow.builder()
                        .followerMember(member1)
                        .followingMember(member2)
                        .build();
                em.persist(follow);
            }
        }

        public void initArticle(){
            for (int i = 0; i < 500 ; i++){
                List<Member> findMembers = memberRepository.findAll();
                Member findMember = findMembers.get(random(findMembers.size() - 1));
                List<Restaurant> findRestaurants = restaurantRepository.findAll();
                Restaurant findRestaurant = findRestaurants.get(random(findRestaurants.size() - 1));

                int num1 = random(dummyContent.length() - 1);
                int num2 = random(dummyContent.length() - 1);
                int small = 0;
                int big = 0;
                if(num1 <= num2){
                    small = num1;
                    big = num2;
                }

                Article article = Article.builder()
                        .member(findMember)
                        .restaurant(findRestaurant)
                        .content(dummyContent.substring(small,big))
                        .image("image"+random(10000))
                        .tagValue(null)
                        .status(ArticleStatus.NORMAL)
                        .build();

                Article savedArticle = articleRepository.save(article);

                List<Tag> findTags = tagRepository.findAll();

                TagValue tagValue = TagValue.builder()
                        .article(savedArticle)
                        .tag(findTags.get(random(findTags.size()-1)))
                        .build();

                tagValueRepository.save(tagValue);
            }
        }



        private Location createLocation(){
            return Location.builder()
                    .oldAddress("oldAddress"+random(100))
                    .address("address"+random(100))
                    .latitude(latitude + Math.random()/100)
                    .longitude(longitude + Math.random()/100)
                    .build();
        }



        private Area createArea(){
            return Area.builder()
                    .sido(sido)
                    .sigoon(sigoon[random(sigoon.length)])
                    .dong(null)
                    .build();
        }

        private Restaurant createRestaurant(){
            return Restaurant.builder()
                    .name("restaurant name"+random(10))
                    .category(category[random(category.length)])
                    .location(createLocation())
                    .area(createArea())
                    .build();
        }

        private Member createMember(){
            return Member.builder()
                    .email(UUID.randomUUID()+"@"+email[random(email.length)]+"com")
                    .passwd("1234")
                    .salt("salt")
                    .name("name"+random(100))
                    .nickName("@user-"+random(1000000000))
                    .status(MemberStatus.NORMAL)
                    .area(createArea())
                    .build();
        }


        String dummyContent = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                "Why do we use it?" +
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
                "Where does it come from?" +
                "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.\n" +
                "Where can I get some?" +
                "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.";



    }


}
