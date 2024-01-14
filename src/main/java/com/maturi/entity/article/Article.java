package com.maturi.entity.article;

import com.maturi.entity.BaseTimeEntity;
import com.maturi.entity.comment.Comment;
import com.maturi.entity.like.LikeArticle;
import com.maturi.entity.member.Member;
import com.maturi.entity.resturant.Restaurant;
import com.maturi.entity.tag.TagValue;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = {"likes","comments"})
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY) // 필요한 경우에만 해당 테이블 join함
  private Member member;
  @ManyToOne(fetch = FetchType.LAZY)
  private Restaurant restaurant;
  @Column(columnDefinition = "TEXT")
  private String content;
  @Column(columnDefinition = "TEXT", nullable = false)
  private String image; // 이미지 여러개 업로드 가능 , 로 구분해서 받음
  private String imageSize;//이미지의 크기
  private int views = 0;//조회수
  @OneToMany(mappedBy = "article")
  private List<TagValue> tagValue;
  @Enumerated(EnumType.STRING)
  private ArticleStatus status;
  @OneToMany(mappedBy = "article")
  private List<LikeArticle> likes = new ArrayList<>();
  @OneToMany(mappedBy = "article")
  private List<Comment> comments = new ArrayList<>();
  public void changeStatus(ArticleStatus status){
    this.status = status;
  }
  public void changeContent(String content){
    this.content = content;
  }
  public void changeImage(String image){
    this.image = image;
  }
  public void changeImageSize(String imageSize){
    this.imageSize = imageSize;
  }
  public void viewCountUp(){
    this.views += 1;
  }
}
