package com.maturi.entity.member;

import com.maturi.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
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
}
