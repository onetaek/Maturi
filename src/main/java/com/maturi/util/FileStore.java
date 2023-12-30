package com.maturi.util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.maturi.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Slf4j
@Component
@RequiredArgsConstructor
public class FileStore {
    private final S3Config s3Config;

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    @Value("${app.firebase-root}")
    private String rootPath;

    @Value("${cloud.aws.s3.bucket}")
    private String awsS3bucket;

    public List<String> storeFiles(List<MultipartFile> multipartFiles, HttpServletRequest request) throws IOException {
        List<String> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(this.storeFile(multipartFile, request));
            }
        }
        return storeFileResult;
    }

    public String storeFile(MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        //UUID의 이미지명 생성
        String storeFileName = this.createStoreFileName(multipartFile.getOriginalFilename());

        //메모리저장소에 이미지 임시로 저장
        String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
        String realPath = contextRoot + "/upload/";
        File file = new File(realPath);// file 저장 경로 자동 생성
        if(!file.exists()){
            file.mkdirs();
        }
        File localFile = new File(realPath + storeFileName);
        multipartFile.transferTo(new File(realPath+storeFileName));

        //aws s3 서비스에 저장요청
        s3Config.amazonS3Client().putObject(new PutObjectRequest(awsS3bucket, storeFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = s3Config.amazonS3Client().getUrl(awsS3bucket, storeFileName).toString();

        //메모리저장소에 임시로 저장했던 이미지 삭제
        localFile.delete();

        return s3Url;
    }

    public String storeFileToFireBase(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        byte[] bytes = multipartFile.getBytes();
        String storeFileName = this.createStoreFileName(multipartFile.getOriginalFilename());
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        ByteArrayInputStream content = new ByteArrayInputStream(bytes);
        bucket.create(storeFileName,content,multipartFile.getContentType());
        return rootPath + firebaseBucket + "/" + storeFileName;
    }

    // 랜덤UUID.확장자명의 형식으로 값을 반환함
    // 확장자명을 붙히는 작업은 없어도 되지만 관리를 편하게하기 위해 붙혀줌
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    //createStoreFileName메서드에서 사용하는 메서드로 확장자명을 분리해주는 메서드이다
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
