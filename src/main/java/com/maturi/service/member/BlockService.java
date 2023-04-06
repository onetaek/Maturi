package com.maturi.service.member;

import com.maturi.dto.member.MemberBlockDTO;
import com.maturi.entity.member.Member;
import com.maturi.repository.member.block.BlockQuerydslRepository;
import com.maturi.repository.member.follow.FollowQuerydslRepository;
import com.maturi.repository.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BlockService {

    private final MemberRepository memberRepository;
    private final BlockQuerydslRepository blockQRepository;
    private final FollowService followService;
    private final FollowQuerydslRepository followQRepository;

    //회원을 차단
    public void blockingMember(Long blockingMemberId, Long blockedMemberId){
        Member blockingMember = memberRepository.findById(blockingMemberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Member blockedMember = memberRepository.findById(blockedMemberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        //유저를 차단
        blockQRepository.saveBlock(blockingMember,blockedMember);
        //유저를 차단하고 나서, 팔로잉하던 유저라면 팔로우 목록에서도 삭제
        if(followQRepository.isFollowingMember(blockingMemberId, blockedMemberId)){
            followService.followingCancel(blockingMemberId,blockedMemberId);
        }
    }

    //차단 목록 조회
    public List<MemberBlockDTO> findBlockMembers(Long memberId){
        List<MemberBlockDTO> blockMembers = blockQRepository.findBlockMembers(memberId);
        for (MemberBlockDTO blockMember : blockMembers) {
            LocalDateTime blockDate = blockMember.getBlockDate();
            blockMember.setFormatDate(blockDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));
        }
        return blockMembers;
    }

    //차단 삭제
    public void removeBlock(Long blockingMemberId, Long blockedMemberId){
        blockQRepository.deleteBlock(blockingMemberId,blockedMemberId);
    }

    //차단한 유저인지 확인
    public boolean checkBlocking(Long blockingMemberId, Long blockedMemberId){
        return blockQRepository.isBlockedMember(blockingMemberId, blockedMemberId);
    }

}
