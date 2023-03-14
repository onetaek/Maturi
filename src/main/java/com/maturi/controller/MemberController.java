package com.maturi.controller;

import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.entity.member.Member;
import com.maturi.service.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

  final private MemberService memberService;
  @GetMapping("/join")
  public String getJoin(){
    return "/member/join";
  }
  @PostMapping("/join")
  public String postJoin(@ModelAttribute MemberJoinDTO memberJoinDTO){

    memberService.join(memberJoinDTO);

    return "redirect:/member/login";
  }

  @GetMapping("/login")
  public String login(){
    return "/member/login";
  }
}
