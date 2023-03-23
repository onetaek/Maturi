package com.maturi.api;

import com.maturi.dto.member.AreaInterDTO;
import com.maturi.repository.member.MemberRepository;
import com.maturi.service.member.EmailService;
import com.maturi.service.member.MemberService;
import com.maturi.util.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberAPIController {

  final private MemberService memberService;
  final private EmailService emailService;

  @PostMapping("/emailAuth")
  public ResponseEntity<String> emailAuth(@RequestBody String json,
                          HttpServletRequest request) throws Exception {
    // parse
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
    String email = (String) jsonObject.get("email");

    /* 이메일 중복 검사 */
    boolean isJoinMember = memberService.emailDuplCheck(email);
    if(isJoinMember){ // 중복된 이메일 존재
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /* 이메일 인증 메일 보내기 */
    String confirm = emailService.sendSimpleMessage(email);

    HttpSession session = request.getSession();
    session.setAttribute("emailConfirm", confirm);
    log.info("emailConfirm Number = {}", confirm);

    return ResponseEntity.status(HttpStatus.OK).build();
  }


  @PostMapping("/emailConfirm")
  public ResponseEntity<String> emailConfirm(@RequestBody String json,
                             HttpServletRequest request) throws ParseException {
    // parse
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
    String emailConfirm = (String) jsonObject.get("emailConfirm");

    HttpSession session = request.getSession();
    String sessionEmailConfirm = (String) session.getAttribute("emailConfirm");

    ResponseEntity response = null;
    if(emailConfirm.equals(sessionEmailConfirm)){
      response = ResponseEntity.status(HttpStatus.OK).build();
      session.removeAttribute("emailConfirm");
    } else {
      response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return response;
  }
  @ResponseBody
  @PatchMapping("/area")
  public ResponseEntity<AreaInterDTO> changeInterestLocation(@Login Long memberId,
                                                               @RequestBody AreaInterDTO areaInterDTO){
    log.info("/api/member/area PATCH요청");
    memberService.changeInsertArea(memberId,areaInterDTO);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
  @ResponseBody
  @GetMapping("/area")
  public ResponseEntity<AreaInterDTO> selectInterestLocation(@Login Long memberId){
    log.info("/api/member/area GET요청");
    return ResponseEntity.status(HttpStatus.OK).body(memberService.selectInterLocation(memberId));
  }

  @ResponseBody
  @DeleteMapping("/area")
  public ResponseEntity<AreaInterDTO> deleteInterestLocation(@Login Long memberId){
    log.info("/api/member/area DELETE요청");
    memberService.removeArea(memberId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/nickNameCheck")
  public ResponseEntity<String> nickNameDuplCheck(@RequestBody String json) throws ParseException {
    // parse
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
    String nickName = (String) jsonObject.get("nickName");

    boolean existed = memberService.nickNameDuplCheck(nickName);
    return existed ?
            ResponseEntity.status(HttpStatus.OK).build() : // 중복 있음
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 중복 없음
  }
}
