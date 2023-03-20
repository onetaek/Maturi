package com.maturi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    //여러 파일을 저장하는 메서드, storeFile()메서드를 반복해서 부르는 식으로 여러 파일을 저장함
    public List<String> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;//파일 ,로 연결해서 구분
    }

    //파일 하나를 저장하는 메서드, new File을 통해 실제 폴더에 파일을 저장하는 로직이 있음
    public String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return storeFileName;
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
