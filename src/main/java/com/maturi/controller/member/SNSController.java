package com.maturi.controller.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.dto.member.MemberSNSLoginDTO;
import com.maturi.entity.member.Member;
import com.maturi.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maturi.service.member.SNSService;
import com.maturi.util.constfield.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static com.maturi.util.constfield.SnsConst.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Controller
public class SNSController {

    final private SNSService snsService;
    final private MemberService memberService;

    @PostMapping("/kakao/login")
    public void loginFirst(HttpServletResponse response){
        try {
            response.sendRedirect("https://kauth.kakao.com/oauth/authorize?" +
                    "client_id=" + KAKAO_REST_API_KEY +
                    "&redirect_uri="+ KAKAO_REDIRECT_URI +"&response_type=code");
        } catch (IOException e) {
            log.info("kakao 로그인 url전송중 에러");
            throw new RuntimeException(e);
        }
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
        String email = snsService.changeIdToKakaoEmail(userInfo.get("uniqueID").toString());

        MemberSNSLoginDTO memberLoginDTO = MemberSNSLoginDTO
                .builder()
                .email(email)
                .profileImg(userInfo.get("profileImg").toString())
                .name(userInfo.get("nickName").toString())
                .build();

        // 2. 가입 & 로그인 나누기
        boolean IsLoginMember = memberService.emailDuplCheck(email);// 중복 이메일 체크


        Member member = null;
        if(!IsLoginMember){// 3-1. 가입하지 않은 회원 -> 가입 진행
            member = snsService.snsJoin(memberLoginDTO);
        } else{// 3-2. 이메일로 중복체크 했을 때 이미 가입했다는 거니까 이메일로 회원을 찾아온다.
            member = snsService.findUser(email);
        }

        // 3-2. 로그인 처리
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MEMBER_ID,member.getId());


        log.info("redirectURL = {}",redirectURL);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpServletRequest request){
        log.info("logout처리 완료!");
        return "/member/login";
    }

    @PostMapping("/naver/login")
    public void naverLogin(HttpServletRequest request, HttpServletResponse response){
        log.info("/naver/login post요청");
        String clientId = NAVER_CLIENT_ID;//애플리케이션 클라이언트 아이디값";
        String redirectURI = null;
        try {
            redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL, "UTF-8");
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

    @GetMapping("/naver/token")//CallBack URL
    public void naverLoginGETToken(HttpServletRequest request,HttpServletResponse response){
        log.info("/naver/login get요청");

        String clientId = NAVER_CLIENT_ID;//애플리케이션 클라이언트 아이디값";
        String clientSecret = NAVER_CLIENT_SECRET;//애플리케이션 클라이언트 시크릿값";
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String redirectURI = null;
        try {
            redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL, "UTF-8");
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

        String access_Token;
        String refresh_Token;

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
                Map<String, Object> jsonMap = objectMapper.readValue(res.toString(), new TypeReference<Map<String, Object>>() {
                });

                access_Token = jsonMap.get("access_token").toString();
                refresh_Token = jsonMap.get("refresh_token").toString();

                log.info("[naver] access_token : " + access_Token);
                log.info("[naver] refresh_token : " + refresh_Token);
                response.sendRedirect("/oauth/naver/login?token="+access_Token);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @GetMapping("/naver/login")
    public String naverLoginProcess(@RequestParam String token,
                                    @RequestParam(defaultValue = "/") String redirectURL,
                                    HttpServletRequest request){

        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();

        HashMap<String, Object> userInfo = null;

        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        String apiURL = "https://openapi.naver.com/v1/nid/me";


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);
        log.info("responseBody = {}",responseBody);


        // jackson objectmapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();
        // JSON String -> Map
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

            log.info("jsonMap = {}",jsonMap);
            userInfo = (HashMap<String, Object>) jsonMap.get("response");

            log.info("[네이버 응답] id = {}",userInfo.get("id"));
            log.info("[네이버 응답] profile_image = {},",userInfo.get("profile_image"));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /* 실질적 로그인 처리 */
        // 1. 이메일 형식으로 변환
        MemberSNSLoginDTO memberLoginDTO = MemberSNSLoginDTO
                .builder()
                .email(snsService.changeNaverIdToEmail((String) userInfo.get("id")) )
                .profileImg((String) userInfo.get("profile_image"))
                .name((String) userInfo.get("name"))
                .build();

        // 2. 가입 & 로그인 나누기
        boolean IsLoginMember = memberService.emailDuplCheck(snsService.changeNaverIdToEmail((String) userInfo.get("id")));// 중복 이메일 체크

        Member member = null;
        if(!IsLoginMember){// 3-1. 가입하지 않은 회원 -> 가입 진행
            member = snsService.snsJoin(memberLoginDTO);
        } else{// 3-2. 이메일로 중복체크 했을 때 이미 가입했다는 거니까 이메일로 회원을 찾아온다.
            member = snsService.findUser(snsService.changeNaverIdToEmail((String) userInfo.get("id")));
        }

        // 3-2. 로그인 처리
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MEMBER_ID,member.getId());


        log.info("redirectURL = {}",redirectURL);
        return "redirect:" + redirectURL;

    }

    private String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    @GetMapping("/naver/logout")
    @ResponseBody
    public void naverLogOutTest(HttpServletResponse response){
        try {
            response.sendRedirect("http://nid.naver.com/nidlogin.logout");
        } catch (IOException e) {
            log.info("naver logout error");
            throw new RuntimeException(e);
        }
    }

}
