package com.maturi.repository.member;

import com.maturi.dto.member.MemberFollowResponse;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.maturi.entity.member.QFollow.*;

@RequiredArgsConstructor
@Repository
public class FollowQuerydslRepository {

    private final JPAQueryFactory query;

    //팔로워를 찾는다.(나를 팔로우해주는 멤버를 찾는다.)
    public List<MemberFollowResponse> findFollowers(Long followingMemberId, String keyword) {

        QMember followingMember = new QMember("followingMember");
        QMember followerMember = new QMember("followerMember");

        BooleanBuilder builder = new BooleanBuilder();
        builder.or(followMemberNameLike(followerMember,keyword))
                .or(followMemberNickNameLike(followerMember,keyword));

        return query.select(Projections.bean(MemberFollowResponse.class,
                        followerMember.id,
                        followerMember.profileImg,
                        followerMember.nickName,
                        followerMember.name,
                        followerMember.profile))
                .from(follow)
                .join(follow.followingMember, followingMember)
                .join(follow.followerMember, followerMember)
                .on(followingMember.id.eq(followingMemberId))
                .where(builder)
                .fetch();
    }

    //팔로잉 멤버를 찾는다.(내가 팔로우하는 멤버를 찾는다)
    public List<MemberFollowResponse> findFollowings(Long followerMemberId, String keyword) {
        QMember followerMember = new QMember("followerMember");
        QMember followingMember = new QMember("followingMember");

        BooleanBuilder builder = new BooleanBuilder();
        builder.or(followMemberNameLike(followingMember,keyword))
                .or(followMemberNickNameLike(followingMember,keyword));

        return query.select(Projections.bean(MemberFollowResponse.class,
                        followingMember.id,
                        followingMember.profileImg,
                        followingMember.nickName,
                        followingMember.name,
                        followingMember.profile))
                .from(follow)
                .join(follow.followerMember, followerMember)
                .join(follow.followingMember, followingMember)
                .on(followerMember.id.eq(followerMemberId))
                .where(builder)
                .fetch();
    }

    //팔로잉 멤버를 찾는다.(내가 팔로우하는 멤버를 찾는다)
    public boolean isFollowingMember(Long followerMemberId,Long followingMemberId) {
        QMember followerMember = new QMember("followerMember");
        QMember followingMember = new QMember("followingMember");

        BooleanBuilder builder = new BooleanBuilder();

        List<Member> followingMembers = query.select(followingMember)
                .from(follow)
                .join(follow.followingMember, followingMember)
                .join(follow.followerMember, followerMember)
                .on(followerMember.id.eq(followerMemberId))
                .on(followingMember.id.eq(followingMemberId))
                .fetch();

        return followingMembers.size() != 0;//true면 팔로우멤버가 맞다.
    }

    private BooleanExpression followMemberNameLike(QMember followMember,String keyword){
        return keyword != null ? followMember.name.contains(keyword) : null;
    }

    private BooleanExpression followMemberNickNameLike(QMember followMember,String keyword){
        return keyword != null ? followMember.nickName.contains(keyword) : null;
    }


}
