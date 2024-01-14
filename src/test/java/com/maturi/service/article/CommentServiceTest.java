package com.maturi.service.article;

import com.maturi.entity.article.Article;
import com.maturi.entity.article.ArticleStatus;
import com.maturi.entity.comment.Comment;
import com.maturi.entity.member.Member;
import com.maturi.repository.comment.CommentQuerydslRepository;
import com.maturi.repository.comment.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@Transactional
@SpringBootTest
class CommentServiceTest {
    @Autowired private CommentRepository commentRepository;
    @Autowired CommentQuerydslRepository commentQRepository;
    @Autowired CommentService commentService;
    @Autowired EntityManager em;

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
    }

    @Test
    @DisplayName("새로운 댓글을 작성했을 경우, ref값과 refStep의 값이 1인지 확인")
    public void writeNewComment() throws Exception{
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "member1")
                .getSingleResult();

        Article article1 = em.createQuery("select a from Article a where a.content = :content", Article.class)
                .setParameter("content", "내용")
                .getSingleResult();

        Long memberId = member1.getId();
        Long articleId = article1.getId();
        Long ref = null;
        Long refStep = null;
        Long refMemberId = null;
        String refMemberNickName = null;
        String content = "안녕하세요";

        //when
        Comment savedComment = commentService.write(memberId, articleId, ref, refStep, refMemberId, refMemberNickName, content);

        //then
        assertThat(savedComment.getMember().getId()).isEqualTo(memberId);
        assertThat(savedComment.getArticle().getId()).isEqualTo(articleId);
        assertThat(savedComment.getRef()).isEqualTo(1L);
        assertThat(savedComment.getRefStep()).isEqualTo(1L);
        assertThat(savedComment.getRefMemberId()).isEqualTo(null);
        assertThat(savedComment.getRefMemberNickName()).isEqualTo(null);
        assertThat(savedComment.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("대댓글의 달았을 경우 Ref값을 유지시키는지, RefStep값이 +1 되는지 확인")
    public void writeRefComment(){
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "member1")
                .getSingleResult();
        Article article1 = em.createQuery("select a from Article a where a.content = :content", Article.class)
                .setParameter("content", "내용")
                .getSingleResult();

        Long memberId = member1.getId();
        Long articleId = article1.getId();
        Long ref = null;
        Long refStep = null;
        Long refMemberId = null;
        String refMemberNickName = null;
        String content = "안녕하세요";
        Comment comment1 = commentService.write(memberId, articleId, ref, refStep, refMemberId, refMemberNickName, content);

        //when
        Comment comment1의_대댓글 = commentService.write(memberId, articleId, comment1.getRef(), comment1.getRefStep(), null, null, "comment1의 대댓글");

        //then
        assertThat(comment1의_대댓글.getRef()).isEqualTo(comment1.getRef());
        assertThat(comment1의_대댓글.getRefStep()).isEqualTo(comment1.getRefStep()+1);
    }

    @Test
    @DisplayName("새로운 댓글을 달았을 경우 ref값이 최대값의 + 1이 되는지, refStep이 1이 되는지 확인")
    void writeMaxRefComment(){
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "member1")
                .getSingleResult();
        Article article1 = em.createQuery("select a from Article a where a.content = :content", Article.class)
                .setParameter("content", "내용")
                .getSingleResult();

        Long memberId = member1.getId();
        Long articleId = article1.getId();
        Long ref = null;
        Long refStep = null;
        Long refMemberId = null;
        String refMemberNickName = null;
        String content = "안녕하세요";
        Comment comment1 = commentService.write(memberId, articleId, ref, refStep, refMemberId, refMemberNickName, content);
        em.flush();
        em.clear();
        log.info("comment1의 값 = {}",comment1);
        //when
        Comment 두번째_새로운_댓글 = commentService.write(memberId, articleId, null, null, null, null, "두번째 새로운 댓글");

        //then
        assertThat(두번째_새로운_댓글.getRef()).isEqualTo(comment1.getRef() + 1);
        assertThat(두번째_새로운_댓글.getRefStep()).isEqualTo(1L);
    }

    @Test
    void getComments(){
        //given
        Member member1 = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "member1")
                .getSingleResult();
        Article article1 = em.createQuery("select a from Article a where a.content = :content", Article.class)
                .setParameter("content", "내용")
                .getSingleResult();
        Long memberId = member1.getId();
        Long articleId = article1.getId();
        Long ref = null;
        Long refStep = null;
        Long refMemberId = null;
        String refMemberNickName = null;
        String content = "안녕하세요";
        Comment comment1 = commentService.write(memberId, articleId, null, null, null, null, "content1");//ref:1, refStep:1
        Comment comment2 = commentService.write(memberId, articleId, comment1.getRef(), comment1.getRefStep(), null, null, "content2");//ref:1, refStep:2
        Comment comment3 = commentService.write(memberId, articleId, comment2.getRef(), comment2.getRefStep(), null, null, "content3");//ref:1, refStep:3
        Comment comment4 = commentService.write(memberId, articleId, null, null, null, null, "content4");//ref:2, refStep:1
        Comment comment5 = commentService.write(memberId, articleId, comment3.getRef(), comment3.getRefStep(), null, null, "content5");//ref:1, refStep:4
        Comment comment6 = commentService.write(memberId, articleId, null, null, null, null, "content6");//ref:3, refStep:1
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);
        commentRepository.save(comment6);

        List<Comment> findComments = commentQRepository.findByArticleId(articleId);
        for (Comment findComment : findComments) {
            log.info("findComment = {}",findComment);
        }
        assertThat(findComments).hasSize(6);


    }
}