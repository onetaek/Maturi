package com.maturi.api.member;


import com.maturi.dto.member.MemberFollowRequest;
import com.maturi.dto.member.MemberFollowResponse;
import com.maturi.service.member.FollowService;
import com.maturi.util.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class FollowAPIController {

    private final FollowService followService;

    //팔로워목록(나를 팔로우하는 사람들)
    @GetMapping("/{id}/follows")
    public List<MemberFollowResponse> selectFollowers(@Login Long memberId,
                                                      @PathVariable Long id,
                                                      @ModelAttribute MemberFollowRequest memberFollowRequest){
        if(memberId != id){ // 다른 유저의 팔로워 목록을 조회하려고 할 때
            throw new RuntimeException("잘못된 사용자");
        }
        return followService.selectFollowerMembers(id);
    }

    //팔로우목록(내가 팔로우 하는 사람들)
    @GetMapping("/{id}/follows")
    public List<MemberFollowResponse> selectFollowings(@Login Long memberId,
                                                       @PathVariable Long id){
        if(memberId != id){ // 다른 유저의 팔로워 목록을 조회하려고 할 때
            throw new RuntimeException("잘못된 사용자");
        }
        return followService.selectFollowingMembers(id);
    }

    //팔로우 추가
    @PostMapping("/{id}/following")
    public ResponseEntity insertFollowering(@Login Long memberId,
                                            @PathVariable Long id,
                                            @RequestBody Long followingMemberId){
        if(memberId != id){ // 다른 유저의 팔로워 목록을 조회하려고 할 때
            throw new RuntimeException("잘못된 사용자");
        }
        log.info("추가할 followingMemberId = {}",followingMemberId);
        followService.following(memberId,followingMemberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //팔로우 삭제
    @DeleteMapping("/{id}/following")
    public ResponseEntity deleteFollowing(@Login Long memberId,
                                          @PathVariable Long id,
                                          @RequestBody Long followingMemberId){
        if(memberId != id){ // 다른 유저의 팔로워 목록을 조회하려고 할 때
            throw new RuntimeException("잘못된 사용자");
        }
        log.info("삭제시킬 followingMemberId = {}",followingMemberId);
        followService.followingCancel(memberId,followingMemberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
