package com.maturi.controller.member;

import com.maturi.dto.member.MemberDetailDTO;
import com.maturi.dto.member.MemberEditMyPageDTO;
import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.dto.member.MemberLoginDTO;
import com.maturi.entity.member.Member;
import com.maturi.service.article.ArticleService;
import com.maturi.service.member.EmailService;
import com.maturi.service.member.MemberService;
import com.maturi.util.argumentresolver.Login;
import com.maturi.util.constfield.MessageConst;
import com.maturi.util.validator.MemberValidator;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.maturi.util.constfield.SessionConst.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
  final private MemberService memberService;
  final private ArticleService articleService;
  final private MemberValidator memberValidator;
  final private EmailService emailService;

  @InitBinder("memberJoinDTO")
  public void init(WebDataBinder dataBinder) {
    dataBinder.addValidators(memberValidator);
  }
  @GetMapping("/join")
  public String getJoin(Model model){
    model.addAttribute("member",new MemberJoinDTO());
    return "/member/join";
  }

  @PostMapping("/join")
  public String join(
          @Validated @ModelAttribute(name = "member") MemberJoinDTO memberJoinDTO,
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
  public String loginPage(@RequestParam(defaultValue = "/") String redirectURL,
                          @RequestParam(defaultValue = "") String successMassage,
                          Model model){

    model.addAttribute("redirectURL",redirectURL);
    model.addAttribute("member",new MemberLoginDTO());
    model.addAttribute("successMsg", successMassage);
    return "/member/login";
  }

  @PostMapping("/login")
  public String login(
          @ModelAttribute(name = "member") MemberLoginDTO memberLoginDTO,
          BindingResult bindingResult,
          @RequestParam(defaultValue = "/") String redirectURL,
          HttpServletRequest request,
          Model model){

    //검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("errors={} ", bindingResult);
      model.addAttribute(MessageConst.ERROR_MESSAGE, MessageConst.LOGIN_FAIL);
      return "/member/login";
    }

    //정상 로직
    Member findMember = memberService.login(memberLoginDTO);
    if(findMember == null){
      model.addAttribute(MessageConst.ERROR_MESSAGE, MessageConst.LOGIN_FAIL);

      return "/member/login";
    }
    else if(memberService.isBanMember(findMember.getId())){ // 밴 멤버
      model.addAttribute(MessageConst.ERROR_MESSAGE, MessageConst.IS_BAN_MEMBER);

      return "/member/login";
    } else { // 정상 멤버
      HttpSession session = request.getSession();
      session.setAttribute(MEMBER_ID,findMember.getId());

      log.info("redirectURL = {}",redirectURL);
      return "redirect:" + redirectURL;
    }
  }

  @PostMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Member LoginMember = memberService.getMemberById((Long) request.getSession().getAttribute(MEMBER_ID));
    request.getSession().invalidate();

    return "redirect:/member/login";
  }

  @PostMapping("/unregister")
  public String unregister(@Login Long memberId,
                           @RequestParam String passwd,
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest request){
    boolean status = memberService.unregister(memberId, passwd);

    if(!status){ // 회원 탈퇴 실패
      return "redirect:/myPage/detail";
    }
    else { // 회원 탈퇴 성공
      // 세션 삭제
      request.getSession().invalidate();
      redirectAttributes.addAttribute(MessageConst.SUCCESS_MESSAGE, "unregister");

      return "redirect:/member/login";
    }
  }
  @GetMapping("/myPage/{id}")
  public String myPage(@Login Long memberId,
                       @PathVariable Long id, // 해당 마이페이지 유저
                       Model model){

    model.addAttribute("member", articleService.memberInfo(memberId));
    model.addAttribute("myPageMember", memberService.myPageMemberInfo(id));
    return "/member/myPage";
  }

  @GetMapping("/myPage/edit")
  public String editMyPage(@Login Long memberId, Model model){

    model.addAttribute("member", articleService.memberInfo(memberId));
    model.addAttribute("myPageMember", memberService.myPageMemberInfo(memberId));
    return "/member/editMyPage";
  }
  @PostMapping("/myPage/edit")
  public String editMemberProfileInfo(@Login Long memberId,
                                      MemberEditMyPageDTO memberEditMyPageDTO,
                                      Model model) throws IOException {

    memberService.editMemberProfileInfo(memberId, memberEditMyPageDTO);

    return "redirect:/member/myPage/" + memberId;
  }

  @GetMapping("/myPage/detail")
  public String myPageDetail(@Login Long memberId,
                             Model model){

    model.addAttribute("member", articleService.memberInfo(memberId));
    model.addAttribute("myPageMember", memberService.myPageMemberInfo(memberId));
    model.addAttribute("memberDetailInfo", memberService.memberDetailInfo(memberId));

    return "/member/myPageDetail";
  }

  @PostMapping("/newPasswd")
  public String newPasswd(@Login Long memberId,
                          @RequestParam String passwd){
    Member member = memberService.changePasswd(memberId, passwd);

    return "redirect:/member/myPage/" + memberId;
  }

}
