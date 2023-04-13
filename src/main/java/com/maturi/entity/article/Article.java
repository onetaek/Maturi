package com.maturi.entity.article;

import com.maturi.entity.BaseTimeEntity;
import com.maturi.entity.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@ToString
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
