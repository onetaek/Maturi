package com.maturi.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCommentDTO {
  private Long id;
  private String profileImg;
  private Long memberId;
  private String name;
  private String nickName;
  private String content;
  private LocalDateTime modifiedDate;
  private int like;
  private boolean isLiked;
}
