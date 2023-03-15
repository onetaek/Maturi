package com.maturi.controller.member;

import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.dto.member.MemberLoginDTO;
import com.maturi.entity.member.Member;
import com.maturi.service.member.MemberService;
import com.maturi.util.MemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
  final private MemberService memberService;
  final private MemberValidator memberValidator;

  @InitBinder("memberJoinRequest")
  public void init(WebDataBinder dataBinder) {
    dataBinder.addValidators(memberValidator);
  }
  @GetMapping("/join")
  public String getJoin(Model model){
    model.addAttribute("member",new MemberJoinDTO());
    return "/member/join";
  }

  @PostMapping("/join")
  public String joinValidation(@Validated @ModelAttribute(name = "member") MemberJoinDTO memberJoinDTO,
                               BindingResult bindingResult){
    log.info("memberJoinRequest = {}", memberJoinDTO.toString());
    memberValidator.validate(memberJoinDTO, bindingResult);

    //검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("errors={} ", bindingResult);
      return "/member/join";
    }

    memberService.join(memberJoinDTO);
    return "redirect:/member/login";
  }

  @GetMapping("/login")
  public String loginPage(){
    return "/member/login";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpServletRequest request){

    Member findMember = memberService.login(memberLoginDTO);
    HttpSession session = request.getSession();
    session.setAttribute("memberId",findMember.getId());


    return "redirect:/";
  }


}
