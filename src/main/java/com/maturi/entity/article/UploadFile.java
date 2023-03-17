package com.maturi.entity.article;

import lombok.*;

import javax.persistence.Embeddable;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Embeddable
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
}
