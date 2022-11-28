package com.java.ymjr.qnoss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file,String module);

    void removeFile(String url);
}
