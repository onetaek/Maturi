package com.maturi.entity;

import com.maturi.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("dirty checking 확인")
    void dirtyCheck(){
        Member member = Member.builder()
                .email("test@naver.com")
                .passwd("1234")
                .name("원택")
                .nickName("돼지")
                .profileImg("test.png")
                .profile("profile1")
                .contact("010-1234-5678")
                .status("정상")
                .build();
        em.persist(member);

        em.flush();
        em.clear();


        List<Member> findMembers = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        for (Member findMember : findMembers) {
            System.out.println("findMember = " + findMember);
        }
//        Member findMember = memberRepository.findById(1L).orElse(null);
//        System.out.println("findMember = " + findMember);


    }

}