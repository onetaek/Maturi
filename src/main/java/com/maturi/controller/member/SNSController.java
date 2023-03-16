package com.maturi.controller.member;

import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.dto.member.MemberLoginDTO;
import com.maturi.entity.member.Member;
import com.maturi.service.member.MemberService;
import com.maturi.service.member.SNSService;
import com.maturi.util.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SNSController {

    final private SNSService snsService;
    final private MemberService memberService;

    @GetMapping("/kakao/login")
    public String loginPage(){
        return "/sns_login/kakao_login";
    }

    // 1번 카카오톡에 사용자 코드 받기(jsp의 a태그 href에 경로 있음)
    // 2번 받은 code를 iKakaoS.getAccessToken로 보냄 ###access_Token###로 찍어서 잘 나오면은 다음단계진행
    // 3번 받은 access_Token를 iKakaoS.getUserInfo로 보냄 userInfo받아옴, userInfo에 nickname, email정보가 담겨있음
    @GetMapping("/kakao/callback")//redirect에서 설정해준 url을 여기 입력
    public String kakaoLogin(@RequestParam(value = "code", required = false) String code,
                             @RequestParam(defaultValue = "/") String redirectURL,
                             HttpServletRequest request) throws Throwable {
        /* 유저 정보 들고오기 */
        // 1번
        System.out.println("code:" + code);

        // 2번
        String access_Token = snsService.getKakaoAccessToken(code);
        System.out.println("###access_Token#### : " + access_Token);
        // 위의 access_Token 받는 걸 확인한 후에 밑에 진행

        // 3번
        HashMap<String, Object> userInfo = snsService.getKakaoUserInfo(access_Token);
        log.info("userInfo = {}", userInfo);

        /* 실질적 로그인 처리 */
        // 1. 이메일 형식으로 변환
        String email = snsService.changeIdToEmail(userInfo.get("uniqueID").toString());

        // 2. 가입 & 로그인 나누기
        MemberLoginDTO memberLoginDTO = memberService.emailDuplCheck(email); // 중복 이메일 체크
        log.info("kakaoLogin memberJoinDTO = {}", memberLoginDTO);
        if(memberLoginDTO == null){ // 3-1. 가입하지 않은 회원 -> 가입 진행
            memberLoginDTO = snsService.kakaoJoin(userInfo);
        }
        // 3-2. 로그인 처리
        Member findMember = memberService.login(memberLoginDTO);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MEMBER_ID,findMember.getId());

        log.info("redirectURL = {}",redirectURL);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpServletRequest request){
        log.info("logout");

        return "/member/login";
    }
}
