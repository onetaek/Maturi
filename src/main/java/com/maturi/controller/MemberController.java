package com.maturi.controller;

import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.dto.member.MemberJoinRequest;
import com.maturi.entity.member.Member;
import com.maturi.service.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

  final private MemberService memberService;
  @GetMapping("/join")
  public String getJoin(Model model){
    model.addAttribute("member",new MemberJoinDTO());
    return "/member/join";
  }
  @PostMapping("/join")
  public String postJoin(
          @ModelAttribute MemberJoinDTO memberJoinDTO,
          @RequestParam(name = "pwCheck") String pwCheck
  ){
    memberService.join(memberJoinDTO);
    return "redirect:/member/login";
  }

//  @PostMapping("/join")
  public String joinValidation(@Validated @ModelAttribute(name = "member") MemberJoinRequest memberJoinRequest,
                               BindingResult bindingResult){
    log.info("validation join start");
    log.info("memberJoinRequest = {}",memberJoinRequest.toString());
    String passwd = memberJoinRequest.getPasswd();
    String passwdCheck = memberJoinRequest.getPasswdCheck();
    if(passwd!=null && passwdCheck!=null){
      if(passwd != passwdCheck){
        bindingResult.reject("passwordCheck","비밀번호 불일치 기본 메시지");
      }
    }
    //검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("errors={} ", bindingResult);
      return "redirect:/member/join";
    }

//    memberService.join(memberJoinDTO);
    return "redirect:/member/login";
  }

  @GetMapping("/login")
  public String login(){
    return "/member/login";
  }
}
