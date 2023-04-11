package com.maturi.dto.article;

import lombok.Data;

@Data
public class CommentRequest {
    private Long ref;
    private Long refStep;
    private String content;
    private Long refMemberId;
    private String refMemberNickName;
}
