package com.maturi.repository.member.block;

import com.maturi.dto.member.MemberBlockDTO;
import com.maturi.entity.member.Block;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.QMember;
import com.maturi.repository.member.member.MemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.maturi.entity.member.QBlock.block;


@RequiredArgsConstructor
@Repository
public class BlockQuerydslRepository {

    private final JPAQueryFactory query;
    private final MemberRepository memberRepository;
    private final EntityManager em;

    //blockingMemberId와 blockedMemberId로 차단내역을 만든다
    public void saveBlock(Member blockingMember, Member blockedMember){
        Block block = Block.builder()
                .blockingMember(blockingMember)
                .blockedMember(blockedMember)
                .build();
        em.persist(block);
    }

    //신고한 유저들을 찾는다.(내가 신고를한 유저를 찾는다.)
    public List<MemberBlockDTO> findBlockMembers(Long blockingMemberId){

        QMember blockingMember = new QMember("blockingMember");
        QMember blockedMember = new QMember("blockedMember");

        return query.select(Projections.bean(MemberBlockDTO.class,
                blockedMember.id,
                blockedMember.profileImg,
                blockedMember.nickName,
                blockedMember.name,
                block.blockDate))
                .from(block)
                .join(block.blockingMember, blockingMember)
                .join(block.blockedMember, blockedMember)
                .on(blockingMember.id.eq(blockingMemberId))
                .fetch();
    }

    //차단한 유저인지 확인하는 메서드
    public boolean isBlockedMember(Long blockingMemberId, Long blockedMemberId){
        QMember blockingMember = new QMember("blockingMember");
        QMember blockedMember = new QMember("blockedMember");

        return !query.select(block)
                .from(block)
                .join(block.blockingMember, blockingMember)
                .join(block.blockedMember, blockedMember)
                .on(block.blockingMember.id.eq(blockingMemberId))
                .on(block.blockedMember.id.eq(blockedMemberId))
                .fetch()
                .isEmpty();//true면 신고한 유저다
    }

    //차단을 해제
    public boolean deleteBlock(Long blockingMemberId, Long blockedMemberId){

        return query.delete(block)
                .where(
                        block.blockingMember.id.eq(blockingMemberId),
                        block.blockedMember.id.eq(blockedMemberId)
                )
                .execute() > 0;//true면 정상 실행, false면 삭제 실패
    }

}
