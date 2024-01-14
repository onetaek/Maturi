package com.maturi.controller.member;

import com.maturi.service.member.BlockService;
import com.maturi.service.member.MemberService;
import com.maturi.common.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static com.maturi.common.constfield.MessageConst.ERROR_MESSAGE;
import static com.maturi.common.constfield.MessageConst.NO_PERMISSION;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class BlockController {

    private final BlockService blockService;
    private final MemberService memberService;

    @GetMapping("/{id}/block")
    public String findBlockMember(@Login Long memberId,
                                  @PathVariable Long id,
                                  Model model,
                                  RedirectAttributes redirectAttributes){

        if(!memberId.equals(id)){ // 다른 회원의 차단목록 이동 요청 들어왔을 경우
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, NO_PERMISSION);
            return "redirect:/members/" + id;
        }

        model.addAttribute("member", memberService.memberInfo(memberId));
        model.addAttribute("myPageMember", memberService.myPageMemberInfo(id));
        model.addAttribute("blockMembers", blockService.findBlockMembers(memberId));

        return "members/block";
    }

    //차단 요청
    @ResponseBody
    @PostMapping("/{id}/block")
    public ResponseEntity blockingMember(@Login Long memberId,
                                         @PathVariable Long id,
                                         @RequestBody Map<String,Long> map){
        Long blockedMemberId = map.get("blockedMemberId");

        // 다른 유저가 차단을 하려할 때
        if(!memberId.equals(id)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        //이미 차단한 유저인지 확인
        if (blockService.checkBlocking(memberId, map.get("blockedMemberId"))){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        //정상 로직
        blockService.blockingMember(memberId, map.get("blockedMemberId"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //차단 삭제요청
    @ResponseBody
    @DeleteMapping("/{id}/block")
    public ResponseEntity removeBlock(@Login Long memberId,
                                 @PathVariable Long id,
                                 @RequestBody Map<String,Long> map){
        // 다른 회원의 차단 요청 들어왔을 경우
        if(!memberId.equals(id)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        blockService.removeBlock(memberId,map.get("blockedMemberId"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
