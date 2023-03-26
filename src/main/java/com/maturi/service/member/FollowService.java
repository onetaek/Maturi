package com.maturi.service.member;

import com.maturi.dto.member.MemberFollowResponse;
import com.maturi.entity.member.Follow;
import com.maturi.entity.member.Member;
import com.maturi.repository.member.FollowQuerydslRepository;
import com.maturi.repository.member.FollowRepository;
import com.maturi.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class FollowService {
    final private MemberRepository memberRepository;
    final private FollowRepository followRepository;
    final private FollowQuerydslRepository followQRepository;

    /**
     * 1번이 3번을 팔로우한다. -> follower : 1 , following : 3 save
     * 1번이 3번을 팔로우를 취소한다. -> follower : 1, following : 3 인 데이터 삭제
     *
     * 3번의 팔로우 목록은? -> 조건절에 follower : 3 인 follow테이블 데이터 (없다)
     * 1번의 팔로우 목록은? -> 조건절에 follower : 1 인 follow테이블 데이터 (3번)
     * 3번의 팔로워 목록은?(3번을 팔로우하고있는 유저) -> 조건절에 follow : 조건절에 following : 3 (1번)
     * 1번의 팔로워 목록은?(1번을 팔로우하고있는 유저) -> 조건절에 follow : 조건절에 following : 1 (없다)
     *
     * 목록을 조회할 때 4가지 경우의 수 처럼 보이지만 메서드는 2개만 필요하다.
     */

    //유저(followerMember)가 다른 유저(followingMember)를 팔로우하는 메서드
    public void following(Long followerMemberId, Long followingMemberId){
        Member followerMember = memberRepository.findById(followerMemberId).orElseThrow(() ->
                new IllegalArgumentException("맴버가 없습니다!"));
        Member followingMember = memberRepository.findById(followingMemberId).orElseThrow(() ->
                new IllegalArgumentException("맴버가 없습니다!"));
        Follow follow = Follow.builder()
                .followerMember(followerMember)
                .followingMember(followingMember)
                .build();
        Follow savedFollow = followRepository.save(follow);
        log.info("savedFollow = {}",savedFollow);
    }
    //나의(followingMember) 팔로워(followerMember)의 정보를 가져오는 메서드(나를 팔로우하고 있는 유저)
    public List<MemberFollowResponse> selectFollowerMembers(Long followingMemberId){
//        List<MemberFollowResponse> followerMembers = followQRepository.findByFollowingMemberId(followingMemberId);
//        return followerMembers;
        return null;
    }

    //나의(followerMember) 팔로우(followingMember)의 정보를 가져오는 메서드(내가 팔로우하고 있는 유저)
    public List<MemberFollowResponse> selectFollowingMembers(Long followerMemberId){
        List<MemberFollowResponse> followingMember = followQRepository.findByFollowerMember(followerMemberId);
        return followingMember;
    }

    //나의(followerMember) 팔로우(followingMember)를 취소 하는 메서드
    public void followingCancel(Long followerMemberId, Long followingMemberId) {
        Follow follow = followRepository.findByFollowerMemberIdAndFollowingMemberId(followerMemberId, followingMemberId);
        followRepository.delete(follow);
    }
}
