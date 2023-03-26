package com.maturi.repository.member;

import com.maturi.entity.member.Follow;
import com.maturi.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FollowRepository extends JpaRepository<Follow,Long> {

    public Follow findByFollowerMemberIdAndFollowingMemberId(Long followerMemberId, Long followingMemberId);
}
