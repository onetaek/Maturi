package com.maturi.util.validator;


import com.maturi.dto.member.MemberJoinDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Slf4j
@Component
public class MemberValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MemberJoinDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberJoinDTO member = (MemberJoinDTO) target;

        String passwd = member.getPasswd();
        String passwdCheck = member.getPasswdCheck();
        if(passwd!=null && passwdCheck!=null){
            if(!passwd.equals(passwdCheck)){
                errors.reject("passwordCheck","비밀번호 불일치 기본 메시지");
            }
        }
    }
}
