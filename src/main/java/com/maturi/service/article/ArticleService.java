package com.maturi.service.article;

import com.maturi.dto.member.MemberDTO;
import com.maturi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {

    final private ModelMapper modelMapper;
    final private MemberRepository memberRepository;

    public MemberDTO memberInfo(Long memberId) {
        return modelMapper.map(memberRepository.findById(memberId).orElse(null), MemberDTO.class);
    }
}
