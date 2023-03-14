package com.maturi.service.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

  @Test
  void randomUUID() {
    String nickname = "user-" + UUID.randomUUID().toString().substring(0, 8);

    log.info(nickname);
  }
}