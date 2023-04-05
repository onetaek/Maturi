package com.maturi.repository.member.member;

import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    public Member findByEmail(String email);
    public Member findByEmailAndPasswd(String email,String passwd);

  Member findByNickName(String nickName);

  Member findByIdAndStatus(Long memberId, MemberStatus ban);

  Member findByContact(String tel);
}
