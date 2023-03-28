package com.maturi.entity.article;

import com.maturi.entity.BaseTimeEntity;
import com.maturi.entity.member.Member;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReport extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY) // 필요한 경우에만 해당 테이블 join함
  private Member member; // 신고한 회원
  @ManyToOne(fetch = FetchType.LAZY)
  private Comment comment;
  @Enumerated(EnumType.STRING)
  private ReportStatus status;
}
