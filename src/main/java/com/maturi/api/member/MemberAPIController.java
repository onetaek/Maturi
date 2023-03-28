package com.maturi.api.member;

import com.maturi.dto.member.AreaInterDTO;
import com.maturi.entity.member.Member;
import com.maturi.repository.member.MemberRepository;
import com.maturi.service.member.EmailService;
import com.maturi.service.member.MemberService;
import com.maturi.service.member.SMSService;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberAPIController {

  final private MemberService memberService;
  final private EmailService emailService;
  private final SMSService smsService;

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

  @PostMapping("/sms/send") // 인증 문자 보내기
  public ResponseEntity phoneAuth(@RequestBody Map<String, String> map,
                                  HttpServletRequest request) {
    // 입력받은 폰번호
    String tel = map.get("tel");

    try { // 이미 가입된 전화번호가 있으면 ...
      if(memberService.usedMemberTel(tel) != null){
        return ResponseEntity.status(HttpStatus.IM_USED).build();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 메세지 보내기
    String code = smsService.sendRandomMessage(tel);

    HttpSession session = request.getSession();
    session.setAttribute("rand", code); // 세션에 인증번호 저장

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/sms/confirm") // 보낸 문자 인증 번호 확인하기
  public ResponseEntity phoneAuthOk(@Login Long memberId,
                                    @RequestBody Map<String, String> map,
                                    HttpServletRequest request) {
    HttpSession session = request.getSession();
    String rand = (String) session.getAttribute("rand");
    String code = map.get("authCode");
    String contect = map.get("tel");

    System.out.println(rand + " : " + code);

    if (rand.equals(code)) { // 문자 인증 성공!!!
      session.removeAttribute("rand"); // 세션 값 지우기
      memberService.registerMemberContact(memberId, contect); // 폰 번호 저장
      return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 문자 인증 실패 ... (인증 번호 불일치)
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

  @PostMapping("/passwdCheck")
  public ResponseEntity<String> passwdCheck(@RequestBody String json,
                                            @Login Long memberId) throws ParseException {
    // parse
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
    String passwd = (String) jsonObject.get("passwd");

    Member member = memberService.passwdCheck(memberId, passwd);
    return member != null?
            ResponseEntity.status(HttpStatus.OK).build() : // 비번 일치
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 비번 불일치
  }

  @PostMapping("/help/pwInquiry/emailAuth") // 이미 존재하는 이메일에 메일 전송
  public ResponseEntity<String> memberEmailAuth(@RequestBody String json,
                                          HttpServletRequest request) throws Exception {
    // parse
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
    String email = (String) jsonObject.get("email");

    /* 이메일 중복 검사 */
    boolean isJoinMember = memberService.emailDuplCheck(email);
    if(!isJoinMember){ // 중복된 이메일 존재하지 않음
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /* 이메일 인증 메일 보내기 */
    String confirm = emailService.sendSimpleMessage(email);

    HttpSession session = request.getSession();
    session.setAttribute("emailConfirm", confirm);
    log.info("emailConfirm Number = {}", confirm);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/help/emailInquiry/sms/send") // 회원 정보에 저장된 폰 번호에 인증 문자 보내기
  public ResponseEntity memberPhoneAuth(@RequestBody Map<String, String> map,
                                  HttpServletRequest request) {
    // 입력받은 폰번호
    String tel = map.get("tel");

    // 폰번호와 일치하는 회원 찾기
    Member findMember = memberService.usedMemberTel(tel);
    if(findMember == null){ // 일치하는 회원 없는 경우 ..
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } else if(findMember.getEmail().contains("@k.com") ||
            findMember.getEmail().contains("@n.com")){ // 소셜 로그인이면
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 메세지 보내기
    String code = smsService.sendRandomMessage(tel);

    HttpSession session = request.getSession();
    session.setAttribute("rand", code); // 세션에 인증번호 저장

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/help/emailInquiry/sms/confirm") // 인증번호 확인
  public ResponseEntity<Map<String, String>> memberPhoneAuthOk(@RequestBody Map<String, String> map,
                                    HttpServletRequest request) {
    HttpSession session = request.getSession();
    String rand = (String) session.getAttribute("rand");
    String code = map.get("authCode");
    String contect = map.get("tel");

    System.out.println(rand + " : " + code);

    Map<String, String> emailMap = new HashMap<>();
    if (rand.equals(code)) { // 문자 인증 성공!!!
      // 폰번호와 일치하는 회원 찾기
      Member findMember = memberService.usedMemberTel(contect);

      // 전송할 이메일 정보 저장
      emailMap.put("email", findMember.getEmail());

      session.removeAttribute("rand"); // 세션 값 지우기
      return ResponseEntity.status(HttpStatus.OK).body(emailMap);
    }

    // 문자 인증 실패 ... (인증 번호 불일치)
    return ResponseEntity.status(HttpStatus.OK).body(emailMap);
  }
}
