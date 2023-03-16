package com.maturi.controller.member;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maturi.KakaoLogin.KakaoService;
import com.maturi.util.constfield.SnsConst;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/oauth/naver/login")
    public void naverLogin(HttpServletRequest request, HttpServletResponse response){
        log.info("/oauth/naver/login post요청");
        String clientId = SnsConst.NAVER_CLIENT_ID;//애플리케이션 클라이언트 아이디값";
        String redirectURI = null;
        try {
            redirectURI = URLEncoder.encode(SnsConst.NAVER_CALLBACK_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("naver login encoding error");
            throw new RuntimeException(e);
        }
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += "&client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&state=" + state;
        request.getSession().setAttribute("state", state);
        try {
            response.sendRedirect(apiURL);
        } catch (IOException e) {
            log.info("naver login sendRedirect 요류");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/oauth/naver/login")
    public String naverLoginProcess(HttpServletRequest request){
        log.info("/oauth/naver/login get요청");

        String clientId = SnsConst.NAVER_CLIENT_ID;//애플리케이션 클라이언트 아이디값";
        String clientSecret = SnsConst.NAVER_CLIENT_SECRET;//애플리케이션 클라이언트 시크릿값";
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String redirectURI = null;
        try {
            redirectURI = URLEncoder.encode(SnsConst.NAVER_CALLBACK_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("naver login redirectURI encoding error");
            throw new RuntimeException(e);
        }
        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + clientId;
        apiURL += "&client_secret=" + clientSecret;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        String access_token = "";
        String refresh_token = "";
        System.out.println("apiURL="+apiURL);
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;
            System.out.print("responseCode="+responseCode);
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            if(responseCode==200) {
                log.info("naver에서 제공 토큰 정보 = {}",res.toString());

                // jackson objectmapper 객체 생성
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON String -> Map
//                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
//                });
//
//                access_Token = jsonMap.get("access_token").toString();
//                refresh_Token = jsonMap.get("refresh_token").toString();
//
//                System.out.println("access_token : " + access_Token);
//                System.out.println("refresh_token : " + refresh_Token);

            }


        } catch (Exception e) {
            System.out.println(e);
        }


        return null;
    }


}
