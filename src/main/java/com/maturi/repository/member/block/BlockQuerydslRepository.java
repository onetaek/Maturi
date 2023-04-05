package com.maturi.repository.member.block;

import com.maturi.entity.member.Member;
import com.maturi.entity.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.maturi.entity.article.QBlock.*;

@RequiredArgsConstructor
@Repository
public class BlockQuerydslRepository {

    private final JPAQueryFactory query;

    //신고한 유저들을 찾는다.(내가 신고를한 유저를 찾는다.)
    public List<Member> findBlockMembers(Long blockingMemberId){

        QMember blockingMember = new QMember("blockingMember");
        QMember blockedMember = new QMember("blockedMember");

        return query.select(blockedMember)
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

    //차단 내역을 찾는 메서드(삭제할 때 사용하기위해 사용)
    public boolean deleteBlock(Long blockingMemberId, Long blockedMemberId){

        return query.delete(block)
                .where(
                        block.blockingMember.id.eq(blockingMemberId),
                        block.blockedMember.id.eq(blockedMemberId)
                )
                .execute() > 0;//true면 정상 실행, false면 삭제 실패
    }

}
