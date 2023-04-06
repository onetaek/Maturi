package com.maturi.dto.member;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberBlockDTO {

    private Long id;
    private String profileImg;
    private String nickName;
    private String name;
    private LocalDateTime blockDate;
    private String formatDate;

}
