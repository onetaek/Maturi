package com.maturi.service.member;

import com.maturi.dto.member.MemberJoinDTO;
import com.maturi.dto.member.MemberLoginDTO;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.repository.member.MemberRepository;
import com.maturi.util.PasswdEncry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class MemberService {

  final private MemberRepository memberRepository;
  final private ModelMapper modelMapper;

  public Member join(MemberJoinDTO memberJoinDTO){

    // 비밀번호 암호화
    memberJoinDTO.setPasswd(getPasswdEncry(memberJoinDTO));

    // 닉네임 난수 생성
    memberJoinDTO.setNickName(getRandomNick());

    /* status 세팅 */
    memberJoinDTO.setStatus(MemberStatus.NORMAL);

    // dto를 entity로 변환
    Member mappedMember = modelMapper.map(memberJoinDTO,Member.class);

    // db에 저장
    Member savedMember = memberRepository.save(mappedMember);

    return savedMember;
  }

  public Member login(MemberLoginDTO memberLoginDTO) {
      String email = memberLoginDTO.getEmail();
      Member findMemberByEmail = memberRepository.findByEmail(email);
      String salt = findMemberByEmail.getSalt();

      String passwd = memberLoginDTO.getPasswd();
      /* 비밀번호 암호화 */
      PasswdEncry passwdEncry = new PasswdEncry();


      // 입력받은 비번 + 난수 => 암호화
      log.info("memberLogin passwd = {}", memberLoginDTO.getPasswd());
      String SHA256Pw =
              memberLoginDTO.getPasswd() != null?
                      passwdEncry.getEncry(memberLoginDTO.getPasswd(), salt) : null;
      memberLoginDTO.setPasswd(SHA256Pw);

      Member findMember = memberRepository.findByEmailAndPasswd(email,SHA256Pw);

      return findMember;
  }

  public Member getMemberById(Long id){
    return memberRepository.findById(id).orElse(null);
  }

  public boolean emailDuplCheck(String email){
    /* 이메일 중복 검사 */
    return memberRepository.findByEmail(email) != null;
  }


  public String getPasswdEncry(MemberJoinDTO memberJoinDTO) {
    /* 비밀번호 암호화 */
    PasswdEncry passwdEncry = new PasswdEncry();
    // 난수 생성 및 dto에 세팅
    String salt = passwdEncry.getSalt();
    memberJoinDTO.setSalt(salt);
    // 입력받은 비번 + 난수 => 암호화
    String SHA256Pw = passwdEncry.getEncry(memberJoinDTO.getPasswd(), salt);
    return SHA256Pw;
  }

  public String getRandomNick() {
    /* 닉네임 난수 생성 */
    boolean duplNick = true; // 닉네임 중복검사에 사용될 변수
    String nickName = null;
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
    return nickName;
  }
}
