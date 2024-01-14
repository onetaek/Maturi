package com.maturi.common.test;

import com.maturi.common.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;

    @ResponseBody
//    @GetMapping("/upload/{filename}")
    public Resource showImg(@PathVariable String filename) throws MalformedURLException {
//        String fullPath = fileStore.getFullPath(filename);
//        UrlResource urlResource = new UrlResource("file:" + fullPath);
//        return urlResource;
        return null;
    }
}
