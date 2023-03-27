package com.maturi.repository.member;

import com.maturi.entity.member.Follow;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.QFollow;
import com.maturi.entity.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.maturi.entity.member.QFollow.*;
import static com.maturi.entity.member.QMember.*;

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
    public List<Member> findFollowMemberById(Long memberId){//테스트함
        //select m from follow f join member m on f.following_member_id = m.following_member_id where f.following_member_id = ?
        return query
                .select(follow.followerMember)
                .from(follow)
                .join(follow.followingMember, member)
                .on(follow.followingMember.id.eq(memberId))
                .fetch();
    }

    //나를 팔로우하는 사람을 찾는 메서드
    public List<Member> findFollowerMemberById(Long memberId){//테스트함
        //select m from follow f join member m on f.following_member_id = m.following_member_id where f.following_member_id = ?
        return query
                .select(follow.followerMember)
                .from(follow)
                .join(follow.followerMember, member)
                .on(follow.followerMember.id.eq(memberId))
                .fetch();
    }


}
