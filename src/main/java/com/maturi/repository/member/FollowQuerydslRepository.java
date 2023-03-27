package com.maturi.repository.member;

import com.maturi.dto.member.MemberFollowResponse;
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
    public List<MemberFollowResponse> findByFollowingMemberId(Long followingMemberId, String keyword) {

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
                .join(follow.followingMember, followerMember)
                .on(followingMember.id.eq(followingMemberId))
                .on(builder)
                .fetch();
    }

    //팔로잉 멤버를 찾는다.(내가 팔로우하는 멤버를 찾는다)
    public List<MemberFollowResponse> findByFollowerMember(Long followerMemberId,String keyword) {
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
                .join(follow.followingMember, followingMember)
                .join(follow.followerMember, followerMember)
                .on(followerMember.id.eq(followerMemberId))
                .on(builder)
                .fetch();
    }

    private BooleanExpression followMemberNameLike(QMember followMember,String keyword){
        return keyword != null ? followMember.name.contains(keyword) : null;
    }

    private BooleanExpression followMemberNickNameLike(QMember followMember,String keyword){
        return keyword != null ? followMember.nickName.contains(keyword) : null;
    }


}
