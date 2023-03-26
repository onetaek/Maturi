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


    public List<MemberFollowResponse> findByFollowingMemberId(Long followingMemberId, String keyword) {

        QMember followingMember = new QMember("followingMember");
        QMember followerMember = new QMember("followerMember");

        BooleanBuilder builder = new BooleanBuilder();
        builder.or(followerMemberNameLike(followerMember,keyword));

        return query.select(Projections.bean(MemberFollowResponse.class,
                        followerMember.id,
                        followerMember.profileImg,
                        followerMember.nickName,
                        followerMember.name,
                        followerMember.profile))
                .from(follow)
                .join(follow.followingMember, followingMember)
                .join(follow.followingMember, followerMember)
                .on(followerMember.id.eq(followingMemberId))
                .on(followerMember.name.contains(keyword))
                .fetch();
    }

    private BooleanExpression followerMemberNameLike(QMember followerMember,String keyword){
        return keyword != null ? followerMember.name.contains(keyword) : null;
    }


    public List<MemberFollowResponse> findByFollowerMember(Long followerMemberId) {
        QMember followerMember = new QMember("followerMember");
        QMember followingMember = new QMember("followingMember");

        return query.select(Projections.bean(MemberFollowResponse.class,
                        followingMember.id,
                        followingMember.profileImg,
                        followingMember.nickName,
                        followingMember.name,
                        followingMember.profile))
                .from(follow)
                .join(follow.followingMember, followingMember)
                .join(follow.followerMember, followerMember)
                .on(followingMember.id.eq(followerMemberId))
                .fetch();
    }
}
