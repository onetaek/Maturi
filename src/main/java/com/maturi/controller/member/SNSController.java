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
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Controller
public class SNSController {

    final private SNSService snsService;
    final private MemberService memberService;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;
    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${naver.cliend.id}")
    private String naverClientId;
    @Value("${naver.client.secret}")
    private String naverClientSecret;
    @Value("${naver.redirect-uri}")
    private String naverRedirectUri;


    /**
     * 네이버 로그인 API 처리
     */
    //카카오톡 로그인 버튼 클릭하면 실행된느 메서드
    @PostMapping("/kakao/login")
    public void loginFirst(HttpServletResponse response){
        try {
            response.sendRedirect("https://kauth.kakao.com/oauth/authorize?" +
                    "client_id=" + kakaoRestApiKey +
                    "&redirect_uri="+ kakaoRedirectUri +"&response_type=code");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //카카오톡에 등록한 콜백경로
    @GetMapping("/kakao/callback")//redirect에서 설정해준 url을 여기 입력
    public String kakaoLogin(@RequestParam(value = "code", required = false) String code,
                             @RequestParam(defaultValue = "/") String redirectURL,
                             HttpServletRequest request) throws Throwable {

        String access_Token = snsService.getKakaoAccessToken(code,kakaoRestApiKey,kakaoRedirectUri);//토큰을 받는다.
        HashMap<String, String> userInfo = snsService.getKakaoUserInfo(access_Token);//유저의 정보를 가져온다.
        String email = snsService.changeKakaoIdToEmail(userInfo.get("uniqueID").toString());//카카오에서 받은 id를 email형식으로 변환한다.
        MemberSNSLoginDTO memberLoginDTO = MemberSNSLoginDTO
                .builder()
                .email(email)
                .profileImg(userInfo.get("profileImg"))
                .name(userInfo.get("nickName"))
                .build();//로그인할때 필요한 정보만들어있는 MemberSNSLoginDTO객체로 만들어준다.

        /** 실질적 로그인 처리 **/
        boolean IsLoginMember = memberService.emailDuplCheck(email);// 중복 이메일 체크
        Member member = null;
        if(IsLoginMember){//이메일로 중복체크 했을 때 이미 가입했다는 거니까 이메일로 회원을 찾아온다.
            member = snsService.findUser(email);
        } else{//가입하지 않은 회원 -> 가입 진행
            member = snsService.snsJoin(memberLoginDTO);
        }
        //로그인 처리
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MEMBER_ID,member.getId());

        return "redirect:" + redirectURL;
    }

    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpServletRequest request){
        request.getSession().invalidate();//로그아웃 처리 : 우리 사이트의 세션만 사라지고, SNS로그인은 유지한다.
        return "members/login";
    }

    /**
     * 네이버 로그인 API 처리
     */
    @PostMapping("/naver/login")
    public void naverLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.sendRedirect(createApiURL(request, response, naverClientId));//apiURL를 생성해서 redirect
        } catch (IOException e) {
            log.info("naver login sendRedirect 요류");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/naver/token")//CallBack URL
    public String naverLoginGETToken(HttpServletRequest request,
                                   @RequestParam String code,
                                   @RequestParam String state,
                                   HttpServletResponse response) {
        String naverAcessToken = createNaverAcessToken(response, naverClientId, naverClientSecret, code, state);

        HashMap<String, String> userInfo = getUserInfo(naverAcessToken);
        MemberSNSLoginDTO memberSNSLoginDTO = MemberSNSLoginDTO.builder()
                .email(snsService.changeNaverIdToEmail(userInfo.get("id")) )
                .profileImg((String) userInfo.get("profile_image"))
                .name((String) userInfo.get("name")).build();

        /** 실질적 로그인 처리 **/
        boolean IsLoginMember = memberService.emailDuplCheck(snsService.changeNaverIdToEmail((String) userInfo.get("id")));// 중복 이메일 체크

        Member member = null;
        if(IsLoginMember){// 이메일이 중복일 경우 이미 가입했다는 것이므로 이메일로 회원을 찾아온다.
            member = snsService.findUser(snsService.changeNaverIdToEmail((String) userInfo.get("id")));
        } else{// 가입하지 않은 회원 -> 가입 진행
            member = snsService.snsJoin(memberSNSLoginDTO);
        }
        // 로그인 처리
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MEMBER_ID,member.getId());

        return "redirect:/";
    }

    //안씀
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


    private String createApiURL(HttpServletRequest request, HttpServletResponse response, String clientId) {
        String redirectURI;
        try {
            redirectURI = URLEncoder.encode(naverRedirectUri, "UTF-8");
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
        return apiURL;
    }

    private String createNaverAcessToken(HttpServletResponse response, String clientId, String clientSecret, String code, String state) {
        String redirectURI;
        try {
            redirectURI = URLEncoder.encode(naverRedirectUri, "UTF-8");
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

                return access_Token;
            }
        } catch (Exception e) {
            log.info("naver token접근 오류");
        }
        return null;
    }

    private HashMap<String, String> getUserInfo(String token) {
        HashMap<String, String> userInfo = null;
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
            userInfo = (HashMap<String, String>) jsonMap.get("response");

            log.info("[네이버 응답] id = {}",userInfo.get("id"));
            log.info("[네이버 응답] profile_image = {},",userInfo.get("profile_image"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userInfo;
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
}
