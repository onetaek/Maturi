package com.maturi.repository.member.block;

import com.maturi.dto.member.MemberBlockDTO;
import com.maturi.entity.member.Block;
import com.maturi.entity.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class BlockQuerydslRepositoryTest {

    @PersistenceContext EntityManager em;
    @Autowired BlockQuerydslRepository blockQRepository;

    @BeforeEach
    void settingMemberAndBlock(){
        Member member1 = Member.builder()
                .email("member1@naver.com")
                .name("member1")
                .nickName("@member1")
                .build();
        Member member2 = Member.builder()
                .email("member2@naver.com")
                .name("member2")
                .nickName("@member2")
                .build();
        Member member3 = Member.builder()
                .email("member3@naver.com")
                .name("member3")
                .nickName("@member3")
                .build();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        Block block1 = Block.builder()
                .blockingMember(member1)
                .blockedMember(member2)
                .build();
        em.persist(block1);
        Block block2 = Block.builder()
                .blockingMember(member1)
                .blockedMember(member3)
                .build();
        em.persist(block2);
    }

    @Test
    @DisplayName("member1이 member2,member3 를 신고했을 때 신고한 유저가 2명인지 확인")
    void findBlockMembers() throws Exception{
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                                .setParameter("name","member1")
                                .getSingleResult();

        //when
        List<MemberBlockDTO> blockMembers = blockQRepository.findBlockMembers(member1.getId());
        for (MemberBlockDTO blockMember : blockMembers) {
            log.info("blockMember.js = {}",blockMember);
        }

        //then
        assertThat(blockMembers).hasSize(2);
    }

    @Test
    @DisplayName("member1이 member2과 member3를 차단했을 때, member2과 member3가 차단회원인지를 확인")
    void isBlockedMember() throws Exception{
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member1")
                .getSingleResult();
        Member member2 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member2")
                .getSingleResult();
        Member member3 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member3")
                .getSingleResult();

        //when
        boolean isBlockMember1 = blockQRepository.isBlockedMember(member1.getId(), member2.getId());
        boolean isBlockMember2 = blockQRepository.isBlockedMember(member1.getId(), member3.getId());

        //then
        assertThat(isBlockMember1).isTrue();
        assertThat(isBlockMember2).isTrue();
    }

    @Test
    @DisplayName("member1이 member2와 member3를 차단하고, member2를 차단목록에서 삭제했을 때, " +
            "차단목록이 1명인지와 차단목록에 남아있는 회원이 member3인지 확인")
    void deleteBlock(){
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member1")
                .getSingleResult();
        Member member2 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member2")
                .getSingleResult();
        Member member3 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member3")
                .getSingleResult();

        //when
        boolean isDeletingSuccess = blockQRepository.deleteBlock(member1.getId(), member2.getId());
        List<MemberBlockDTO> blockMembers = blockQRepository.findBlockMembers(member1.getId());

        //then
        assertThat(isDeletingSuccess).isTrue();
        assertThat(blockMembers.get(0).getId()).isEqualTo(member3.getId());
        assertThat(blockMembers).hasSize(1);

    }

    @Test
    @DisplayName("member2가 member1,3를 차단했을 때 차단 회원이 2명인지 확인")
    void saveBlock(){
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member1")
                .getSingleResult();
        Member member2 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member2")
                .getSingleResult();
        Member member3 = em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name","member3")
                .getSingleResult();

        blockQRepository.saveBlock(member2,member1);
        blockQRepository.saveBlock(member2,member3);

        //when
        List<MemberBlockDTO> blockMembers = blockQRepository.findBlockMembers(member2.getId());
        for (MemberBlockDTO blockMember : blockMembers) {
            log.info("blockMembers = {}",blockMember);
        }

        //then
        assertThat(blockMembers).hasSize(2);
    }
}