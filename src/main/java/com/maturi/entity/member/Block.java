package com.maturi.entity.member;

import com.maturi.entity.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 게시글에 차단한 회원의 게시글은 보이지 않도록 할 것이다.
 * 차단 버튼을 클릭하면 게시글을 다시로드해서 보이지않도록
 * 차단 목록 페이지에서 차단을 해제하면 다시 차단목록 불러오고,
 * 게시글에는 다시 해당유저의 게시글이 보일 것이다.
 */
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member blockingMember;//차단을 하는 유저
    @ManyToOne(fetch = FetchType.LAZY)
    private Member blockedMember;//차단을 당하는 유저
    @CreatedDate
    private LocalDateTime blockDate;//차단한 날짜
}
