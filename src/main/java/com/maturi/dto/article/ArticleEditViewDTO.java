package com.maturi.dto.article;

import com.maturi.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEditViewDTO {
    private Long id;
    private Long memberId;
    private String name;
    private String content;
    private List<String> image;
    private Long imageSize;
    // 태그 리스트
    private List<String> tags;
}
