package com.maturi.service.member;

import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@Slf4j
@SpringBootTest
class MemberServiceTest {

  @Autowired ModelMapper modelMapper;

  @Test
  void randomUUID() {
    String nickname = "user-" + UUID.randomUUID().toString().substring(0, 8);

    log.info(nickname);
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
    log.info("mappedMember = {}",mappedMember);

    //then
    Assertions.assertEquals(mappedMember.getEmail(),email);
    Assertions.assertEquals(mappedMember.getPasswd(),passwd);
    Assertions.assertEquals(mappedMember.getSalt(),salt);
    Assertions.assertEquals(mappedMember.getName(),name);
    Assertions.assertEquals(mappedMember.getNickName(),nickName);
    Assertions.assertEquals(mappedMember.getStatus(),status);

  }
}