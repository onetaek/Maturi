package com.maturi.dto.member;

import com.maturi.entity.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSNSJoinDTO {

    private String email;
    private String name;
    private String profileImg;
    private String nickName;
    private MemberStatus status;

}
