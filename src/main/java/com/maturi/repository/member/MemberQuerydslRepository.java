package com.maturi.repository.member;

import com.maturi.entity.member.Member;
import com.maturi.entity.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.maturi.entity.member.QFollow.*;

@RequiredArgsConstructor
@Repository
public class MemberQuerydslRepository {

    private final JPAQueryFactory query;

    /**
     * 햇갈리는게있는데 MySQL기준으로 아래 두개 같은건가요?
     * select * from Member m join Article a on m.id = a.member_id where m.id = 1
     * select * from Member m join Article a on m.id = a.member_id where a.member_id = 1
     */
    //내가 팔로우 하고 있는 사람을 찾는 메서드
    public List<Member> findFollowingsById(Long followerMemberId){//테스트함
        QMember followerMember = new QMember("followerMember");
        QMember followingMember = new QMember("followingMember");

        return query.select(followingMember)
                .from(follow)
                .join(follow.followerMember, followerMember)
                .join(follow.followingMember, followingMember)
                .on(followerMember.id.eq(followerMemberId))
                .fetch();

    }




}
