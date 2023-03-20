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
  private String image; // 이미지 여러개 업로드 가능
  @OneToMany(mappedBy = "article")
  private List<TagValue> tagValue;

  @Enumerated(EnumType.STRING)
  private ArticleStatus status;
}
