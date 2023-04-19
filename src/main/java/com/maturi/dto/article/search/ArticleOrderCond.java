package com.maturi.dto.article.search;

import lombok.Data;

@Data
public class ArticleOrderCond {

    private String orderBy;//최신순, 오래된순, 조회수순, 댓글순, 좋아요순
    private Integer views;//조회수
    private Integer commentCount;//댓글 갯수
    private Integer likeCount;//좋아요 갯수
}
