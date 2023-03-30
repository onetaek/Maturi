package com.maturi.service.member;

import com.maturi.dto.member.MemberFollowRequest;
import com.maturi.dto.member.MemberFollowResponse;
import com.maturi.entity.member.Follow;
import com.maturi.entity.member.Member;
import com.maturi.repository.member.FollowQuerydslRepository;
import com.maturi.repository.member.FollowRepository;
import com.maturi.repository.member.MemberRepository;
import com.maturi.util.constfield.FollowConst;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.maturi.util.constfield.FollowConst.*;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class FollowService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final FollowQuerydslRepository followQRepository;

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

    //나의(followingMember) 팔로워(followerMember)의 정보를 가져오는 메서드(나를 팔로우하고 있는 유저)
    //나의(followerMember) 팔로우(followingMember)의 정보를 가져오는 메서드(내가 팔로우하고 있는 유저)
    public List<MemberFollowResponse> selectFollowMembers(MemberFollowRequest memberFollowRequest, Long followMemberId){
        String follow = memberFollowRequest.getFollow();
        String keyword = memberFollowRequest.getKeyword();

        switch (follow){
            case follower://팔로워 버튼 클릭시
                List<MemberFollowResponse> followers = followQRepository.findFollowers(followMemberId, keyword);
                for (MemberFollowResponse follower : followers) {
                    boolean isFollowingMember = followQRepository.isFollowingMember(followMemberId, follower.getId());
                    follower.setFollowingMember(isFollowingMember);//내가 팔로워를 팔로잉하고있는지 확인
                }
                return followers;
            case following://팔로우 버튼 클릭시
                return followQRepository.findFollowings(followMemberId,keyword);
        }
        return null;
    }

    //유저(followerMember)가 다른 유저(followingMember)를 팔로우하는 메서드
    public boolean following(Long followerMemberId, Long followingMemberId){
        boolean isFollowingMember = followQRepository.isFollowingMember(followerMemberId, followingMemberId);
        log.info("isFollowingMember = {}",isFollowingMember);
        if(isFollowingMember){
            log.info("이미 팔로잉하고 있는 멤버입니다.");
            return false;
        }
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
        return true;
    }

    //나의(followerMember) 팔로우(followingMember)를 취소 하는 메서드
    public void followingCancel(Long followerMemberId, Long followingMemberId) {
        Follow follow = followRepository.findByFollowerMemberIdAndFollowingMemberId(followerMemberId, followingMemberId);
        followRepository.delete(follow);
    }
}
