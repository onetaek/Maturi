package com.maturi.entity.article;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;
@ToString
@Getter
@Embeddable
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;


    //임시로 만듬 -> 프로젝트에서 DTO로 구현할꺼
    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
    public UploadFile() {

    }
}
