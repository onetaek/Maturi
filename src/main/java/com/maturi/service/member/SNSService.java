package com.maturi.service.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maturi.dto.member.MemberSNSJoinDTO;
import com.maturi.dto.member.MemberSNSLoginDTO;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.repository.MemberRepository;
import com.maturi.util.constfield.SnsConst;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.transaction.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class SNSService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    public String getKakaoAccessToken(String authorize_code) throws Exception {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");

            sb.append("&client_id="+ SnsConst.KAKAO_REST_API_KEY); // REST_API키 본인이 발급받은 key 넣어주기
            sb.append("&redirect_uri=" + SnsConst.KAKAO_REDIRECT_URI); // REDIRECT_URI 본인이 설정한 주소 넣어주기

            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            // jackson objectmapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON String -> Map
            Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });

            access_Token = jsonMap.get("access_token").toString();
            refresh_Token = jsonMap.get("refresh_token").toString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_Token;
    }


    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getKakaoUserInfo(String access_Token) throws Throwable {
        // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            System.out.println("result type" + result.getClass().getName()); // java.lang.String

            try {
                // jackson objectmapper 객체 생성
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON String -> Map
                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
                });

                System.out.println(jsonMap.get("properties"));

                Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
                Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

                // System.out.println(properties.get("nickname"));
                // System.out.println(kakao_account.get("email"));

                String nickName = properties.get("nickname").toString();
                String profileImg = properties.get("profile_image").toString();
                String uniqueID = jsonMap.get("id").toString();

                userInfo.put("nickName", nickName);
                userInfo.put("profileImg", profileImg);
                userInfo.put("uniqueID", uniqueID);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
    public String changeIdToKakaoEmail(String uniqueId){
        /* uniqueId를 email형식으로 변환 */
        return uniqueId.toString() + "@k.com";
    }

    public Member snsJoin(MemberSNSLoginDTO memberLoginDTO){

        // API로 받은 로그인 정보를 회원가입하기 위한 정보로 변환
        MemberSNSJoinDTO memberJoinDTO = modelMapper.map(memberLoginDTO,MemberSNSJoinDTO.class);

        // 비밀번호는 필요없음

        // 닉네임 난수 생성
        memberJoinDTO.setNickName(memberService.getRandomNick());

        // status 세팅
        memberJoinDTO.setStatus(MemberStatus.NORMAL);

        // dto를 entity로 변환
        Member mappedMember = modelMapper.map(memberJoinDTO,Member.class);

        // db에 저장
        Member savedMember = memberRepository.save(mappedMember);

        return savedMember;
    }


    public Member findUser(String email) {
        return memberRepository.findByEmail(email);
    }

    public String changeNaverIdToEmail(String email) {
        return email + "@n.com";
    }
}
