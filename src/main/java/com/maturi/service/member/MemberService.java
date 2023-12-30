package com.maturi.service.member;

import com.maturi.dto.member.*;
import com.maturi.entity.member.Area;
import com.maturi.entity.member.Member;
import com.maturi.entity.member.MemberStatus;
import com.maturi.repository.member.follow.FollowQuerydslRepository;
import com.maturi.repository.member.member.MemberRepository;
import com.maturi.util.FileStore;
import com.maturi.util.PasswdEncry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  private final ModelMapper modelMapper;

  private final FileStore fileStore;
  private final FollowQuerydslRepository followQRepository;

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

      if(findMemberByEmail == null){ // 해당되는 아이디 없으면 ..
        return null;
      }

      String salt = findMemberByEmail.getSalt();

      // 비밀번호 암호화
      PasswdEncry passwdEncry = new PasswdEncry();

      // 입력받은 비번 + 난수 => 암호화
      log.info("memberLogin passwd = {}", memberLoginDTO.getPasswd());
      String SHA256Pw = memberLoginDTO.getPasswd() != null?
                      passwdEncry.getEncry(memberLoginDTO.getPasswd(), salt) : null;
      memberLoginDTO.setPasswd(SHA256Pw);

      Member findMember = memberRepository.findByEmailAndPasswd(email,SHA256Pw);

      return findMember;
  }

  public MemberDTO memberInfo(Long memberId) {
    return modelMapper.map(memberRepository.findById(memberId).orElse(null), MemberDTO.class);
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
    // 입력받은 비밀번호 + 난수 => 암호화
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

  public void changeInsertArea(Long memberId, AreaInterDTO areaInterDTO) {
      Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
              new IllegalArgumentException("맴버가 없습니다!"));
      findMember.changeInterArea(modelMapper.map(areaInterDTO, Area.class));
  }

  public AreaInterDTO selectInterLocation(Long memberId) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
            new IllegalArgumentException("맴버가 없습니다!"));
    if(findMember.getArea() == null){
        return null;
    }else{
        return modelMapper.map(findMember.getArea(), AreaInterDTO.class);
    }
  }

  public void removeArea(Long memberId) {
      Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
              new IllegalArgumentException("맴버가 없습니다!"));
      findMember.removeArea();
  }

  public MemberMyPageDTO myPageMemberInfo(Long memberId) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));

    return modelMapper.map(findMember, MemberMyPageDTO.class);
  }

  public boolean nickNameDuplCheck(String nickName) {
    Member findMember = memberRepository.findByNickName(nickName);

    if(findMember == null){
      return false;
    } else {
      return true;
    }
  }

  public void editMemberProfileInfo(Long memberId,
                                    MemberEditMyPageDTO memberEditMyPageDTO,
                                    HttpServletRequest request) throws IOException {
    Member findMember = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));


    findMember.changeSimpleInfo(memberEditMyPageDTO.getNickName(),
                                memberEditMyPageDTO.getName(),
                                memberEditMyPageDTO.getProfile());

    // 이미지 바꿨을 경우 (파일 업로드 로직)
    if(!memberEditMyPageDTO.getCoverImg().isEmpty()) {
      String storeCoverImg = fileStore.storeFileToAwsS3(memberEditMyPageDTO.getCoverImg(), request);
      findMember.changeCoverImg(storeCoverImg);
    }
    if(!memberEditMyPageDTO.getProfileImg().isEmpty()){
      String storeprofileImg = fileStore.storeFileToAwsS3(memberEditMyPageDTO.getProfileImg(), request);
      findMember.changeProfileImg(storeprofileImg);
    }

    log.info("editMemberInfo = {}", findMember);

    memberRepository.save(findMember);
  }

  public boolean isBanMember(Long member_id){ // 밴된 멤버 -> true
    Member findMember = memberRepository.findByIdAndStatus(member_id, MemberStatus.BAN);

    return findMember != null;
  }

  public MemberDetailDTO memberDetailInfo(Long memberId) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));

    MemberDetailDTO memberDetailDTO = modelMapper.map(findMember, MemberDetailDTO.class);

    // 소셜 로그인 => 해당 SNS명을 저장
    if(memberDetailDTO.getEmail().contains("@k.com")){ // 카카오 로그인
      memberDetailDTO.setEmail("KAKAO Login");
    } else if(memberDetailDTO.getEmail().contains("@n.com")){ // 네이버 로그인
      memberDetailDTO.setEmail("NAVER Login");
    }

    return memberDetailDTO;
  }

  public Member passwdCheck(Long memberId, String passwd) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));

    String salt = findMember.getSalt();

    /* 비밀번호 암호화 */
    PasswdEncry passwdEncry = new PasswdEncry();

    // 입력받은 비번 + 난수 => 암호화
    String SHA256Pw = (passwd != null)?
                    passwdEncry.getEncry(passwd, salt) : null;

    // db에 저장된 패스워드
    String findPasswd = findMember.getPasswd();

    return findPasswd.equals(SHA256Pw)? findMember : null;
  }

  public boolean unregister(Long memberId, String passwd) {
    Member findMember = passwdCheck(memberId, passwd);
    if(findMember == null){
      return false;
    }
    else {
      // status -> WITHDRAW 로 수정
      findMember.changeStatus(MemberStatus.WITHDRAW);
      // db에 수정된 사항 저장
      memberRepository.save(findMember);
      return true;
    }
  }

  public Member changePasswd(Long memberId, String passwd) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));

    String salt = findMember.getSalt();

    /* 비밀번호 암호화 */
    PasswdEncry passwdEncry = new PasswdEncry();
    // 입력받은 비번 + 난수 => 암호화
    String SHA256Pw = passwdEncry.getEncry(passwd, salt);

    findMember.changePasswd(SHA256Pw); // 비번 수정
    Member member = memberRepository.save(findMember); // db에 재저장

    return member;
  }

  public Member getMemberByEmail(String email) {
    return memberRepository.findByEmail(email);
  }


  /**
   * 이미 사용중인 휴대폰 번호인지 확인
   * @param tel (입력받은 휴대폰 번호)
   * @return boolean (이미 사용중이면 true)
   */
  public Member usedMemberTel(String tel) {
    Member findMember = memberRepository.findByContact(tel);

    return findMember;
  }

  public void registerMemberContact(Long memberId, String contact) {
    Member findMember = memberRepository.findById(memberId).orElseThrow(()->
            new IllegalArgumentException("맴버가 없습니다!"));

    findMember.changeContact(contact); // 폰 번호 저장
    memberRepository.save(findMember); // db 업데이트
  }

  public boolean checkFollowing(Long followerMemberId, Long followingMemberId) {
    return followQRepository.isFollowingMember(followerMemberId, followingMemberId);
  }
}
