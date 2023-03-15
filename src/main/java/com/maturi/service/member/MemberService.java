package com.maturi.service.member;

import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.dto.member.MemberLoginDTO;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.repository.MemberRepository;
import com.maturi.util.PasswdEncry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class MemberService {

  final private MemberRepository memberRepository;
  final private ModelMapper modelMapper;

  public void join(MemberJoinDTO memberJoinDTO){

    /* 비밀번호 암호화 */
    PasswdEncry passwdEncry = new PasswdEncry();
    // 난수 생성 및 dto에 세팅
    String salt = passwdEncry.getSalt();
    memberJoinDTO.setSalt(salt);
    // 입력받은 비번 + 난수 => 암호화
    String SHA256Pw = passwdEncry.getEncry(memberJoinDTO.getPasswd(), salt);
    memberJoinDTO.setPasswd(SHA256Pw);

    /* 닉네임 난수 생성 */
    String nickName = "@user-" + UUID.randomUUID().toString().substring(0, 8);
    memberJoinDTO.setNickName(nickName);

    /* status 세팅 */
    memberJoinDTO.setStatus(MemberStatus.NORMAL);

    log.info("memberJoinDTO = {}",memberJoinDTO.toString());
    // dto를 entity로 변환
    Member mappedMember = modelMapper.map(memberJoinDTO,Member.class);
    log.info("DTO -> Entity : member  = {}",mappedMember.toString());
    // db에 저장
    memberRepository.save(mappedMember);
  }

    public Member login(MemberLoginDTO memberLoginDTO) {
      String email = memberLoginDTO.getEmail();
      Member findMemberByEmail = memberRepository.findByEmail(email);
      String salt = findMemberByEmail.getSalt();

      String passwd = memberLoginDTO.getPasswd();
      /* 비밀번호 암호화 */
      PasswdEncry passwdEncry = new PasswdEncry();


      // 입력받은 비번 + 난수 => 암호화
      String SHA256Pw = passwdEncry.getEncry(memberLoginDTO.getPasswd(), salt);
      memberLoginDTO.setPasswd(SHA256Pw);

      Member findMember = memberRepository.findByEmailAndPasswd(email,SHA256Pw);

      return findMember;
    }
}
