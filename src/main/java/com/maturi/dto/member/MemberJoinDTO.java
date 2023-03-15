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
public class MemberJoinDTO {

    //유효성 검사하는 필드
    @Email @NotBlank
    private String email;
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}")
    private String passwd;
    @NotBlank
    private String passwdCheck;
    @Min(2)
    private String name;

    //유효성 검사하지 않는 필드
    private String salt;
    private String nickName;
    private MemberStatus status;

}
