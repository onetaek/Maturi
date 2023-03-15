package com.maturi.controller.member;

import com.maturi.KakaoLogin.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SNSController {

    final private KakaoService kakaoService;

    @GetMapping("/kakao/login")
    public String loginPage(){
        return "/sns_login/kakao_login";
    }

    // 1번 카카오톡에 사용자 코드 받기(jsp의 a태그 href에 경로 있음)
    // 2번 받은 code를 iKakaoS.getAccessToken로 보냄 ###access_Token###로 찍어서 잘 나오면은 다음단계진행
    // 3번 받은 access_Token를 iKakaoS.getUserInfo로 보냄 userInfo받아옴, userInfo에 nickname, email정보가 담겨있음
    @GetMapping("/kakao/callback")//redirect에서 설정해준 url을 여기 입력
    public String kakaoLogin(@RequestParam(value = "code", required = false) String code) throws Throwable {
        // 1번
        System.out.println("code:" + code);

        // 2번
        String access_Token = kakaoService.getAccessToken(code);
        System.out.println("###access_Token#### : " + access_Token);
        // 위의 access_Token 받는 걸 확인한 후에 밑에 진행

        // 3번
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);

        //로그인 처리

        return "/article/welcome";
    }

    @GetMapping("/kakao/logout")
    public String kakaoLogout(){
        log.info("logout");
        return "/member/login";
    }
}
