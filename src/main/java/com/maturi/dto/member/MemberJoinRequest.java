package com.maturi.dto.member;

import com.maturi.entity.member.MemberStatus;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
public class MemberJoinRequest {

    @Email
    private String email;
    @NotBlank
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}")
    private String passwd;
    @NotBlank
    private String passwdCheck;
    @NotBlank
    private String name;
}
