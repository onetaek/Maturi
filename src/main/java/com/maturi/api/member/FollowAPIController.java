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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class FollowAPIController {

    private final FollowService followService;



    //팔로워목록(나를 팔로우하는 사람들)
    //팔로우목록(내가 팔로우 하는 사람들)
    @GetMapping("/{id}/follows")
    public ResponseEntity<List<MemberFollowResponse>> selectFollowings(@Login Long memberId,
                                                       @PathVariable Long id,
                                                       @ModelAttribute MemberFollowRequest memberFollowRequest){
        log.info("memberFollowRequest = {}",memberFollowRequest);

        if(!memberId.equals(id)){ // 다른 유저의 팔로워 목록을 조회하려고 할 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<MemberFollowResponse> response = followService.selectFollowMembers(memberFollowRequest, id);//정상 로직
//        if(response == null || response.size() == 0){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
        log.info("response = {}",response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //팔로우 추가
    @PostMapping("/{id}/following")
    public ResponseEntity addFollowing(@Login Long memberId,
                                       @PathVariable Long id,
                                       @RequestBody Map<String, Long> map){
        Long followingMemberId = map.get("followingMemberId");
        if(!memberId.equals(id)){ // 다른 유저의 팔로워 목록을 조회하려고 할 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean isSuccess = followService.following(memberId, followingMemberId);
        if (isSuccess){//성공적으로 팔로잉에 성공
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{//이미 팔로우한 유저가 있어서 실패
            return ResponseEntity.status(HttpStatus.IM_USED).build();
        }
    }

    //팔로우 삭제
    @DeleteMapping("/{id}/following")
    public ResponseEntity removeFollowing(@Login Long memberId,
                                          @PathVariable Long id,
                                          @RequestBody Map<String, Long> map){
        log.info("팔로우 취소 컨트롤러 시작");
        Long followingMemberId = map.get("followingMemberId");
        if(!memberId.equals(id)){ // 다른 유저의 팔로워 목록을 조회하려고 할 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("삭제시킬 followingMemberId = {}",followingMemberId);
        followService.followingCancel(memberId,followingMemberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
