package com.maturi.service.member;

import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.repository.member.member.MemberRepository;
import com.maturi.common.util.PasswdEncry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class MemberServiceTest {

  @Autowired ModelMapper modelMapper;
  @Autowired
  MemberRepository memberRepository;

  @Test
  void randomUUID() {
    String nickname = "user-" + UUID.randomUUID().toString().substring(0, 8);
  }

  @Test
  @DisplayName("ModelMapper를 이용하여 DTO를 Entity로 변환")
  void dtoToEntity(){

    //given
    String email = "test@naver.com";
    String passwd = "tesr13213";
    String salt = "tesfdsaf12";
    String name = "오원택";
    String nickName = "user-test1234";
    MemberStatus status = MemberStatus.NORMAL;

    MemberJoinDTO memberJoinDTO = MemberJoinDTO.builder()
            .email(email)
            .passwd(passwd)
            .salt(salt)
            .name(name)
            .nickName(nickName)
            .status(status)
            .build();

    //when
    Member mappedMember = modelMapper.map(memberJoinDTO, Member.class);

    //then
    Assertions.assertEquals(mappedMember.getEmail(),email);
    Assertions.assertEquals(mappedMember.getPasswd(),passwd);
    Assertions.assertEquals(mappedMember.getSalt(),salt);
    Assertions.assertEquals(mappedMember.getName(),name);
    Assertions.assertEquals(mappedMember.getNickName(),nickName);
    Assertions.assertEquals(mappedMember.getStatus(),status);

  }

  @Test
  void testJoin() {
    MemberJoinDTO memberJoinDTO = MemberJoinDTO.builder()
            .email("test333@abc.com")
            .passwd("Qwer1234!")
            .build();

    /* 비밀번호 암호화 */
    PasswdEncry passwdEncry = new PasswdEncry();
    // 난수 생성 및 dto에 세팅
    String salt = passwdEncry.getSalt();
    memberJoinDTO.setSalt(salt);
    // 입력받은 비번 + 난수 => 암호화
    String SHA256Pw = passwdEncry.getEncry(memberJoinDTO.getPasswd(), salt);
    memberJoinDTO.setPasswd(SHA256Pw);

    /* 닉네임 난수 생성 */
    String nickName = null; // 닉네임이 담길 변수
    boolean duplNick = true; // 닉네임 중복검사에 사용될 변수
    while (duplNick){
      duplNick = false;
      nickName = "@user-" + UUID.randomUUID().toString().substring(0, 8);
      List<Member> memberList = memberRepository.findAll();
      for(Member member : memberList){
        if(nickName.equals(member.getNickName())){
          duplNick = true;
        }
      }
    }
    memberJoinDTO.setNickName(nickName);

    /* status 세팅 */
    memberJoinDTO.setStatus(MemberStatus.NORMAL);

    // dto를 entity로 변환
    Member mappedMember = modelMapper.map(memberJoinDTO,Member.class);
    log.info("DTO -> Entity : member  = {}",mappedMember.toString());
  }
}