package com.maturi.repository.article;

import com.maturi.entity.article.Article;
import com.maturi.entity.article.ArticleStatus;
import com.maturi.entity.article.Comment;
import com.maturi.entity.member.Member;
import com.maturi.service.article.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class CommentQuerydslRepositoryTest {

    @Autowired CommentQuerydslRepository commentQRepository;
    @Autowired ArticleRepository articleRepository;
    @Autowired EntityManager em;
    @Autowired CommentService commentService;


    @BeforeEach
    public void initDate(){
        Member member1 = Member.builder()
                .email("member1@naver.com")
                .name("member1")
                .nickName("member1NickName")
                .build();
        em.persist(member1);
        Member member2 = Member.builder()
                .email("member2@naver.com")
                .name("member2")
                .nickName("member2NickName")
                .build();
        em.persist(member2);
        Article article1 = Article.builder()
                .member(member1)
                .content("내용")
                .image("image1")
                .status(ArticleStatus.NORMAL)
                .build();
        em.persist(article1);

        Comment comment1 = Comment.builder()
                .article(article1)
                .ref(1L)
                .build();
        Comment comment2 = Comment.builder()
                .article(article1)
                .ref(2L)
                .build();
        em.persist(comment1);
        em.persist(comment2);
    }

    @DisplayName("새로운 댓글을 2개 작성해서 최대 ref값이 2일 경우, maxRef값이 2인지 확인")
    @Test void findMaxRef(){
        //given
        Article article1 = em.createQuery("select a from Article a where a.content = :content", Article.class)
                .setParameter("content", "내용")
                .getSingleResult();
        Long id = article1.getId();

        //when
        Long maxRef = commentQRepository.findMaxRef(id);

        //then
        Assertions.assertThat(maxRef).isEqualTo(2L);

    }
}