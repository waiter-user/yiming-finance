package com.java.ymjr.qnoss.service.impl;

import com.java.ymjr.qnoss.service.FileService;
import com.java.ymjr.qnoss.utils.QiniuUtil;
import com.java.ymjr.qnoss.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private QiniuUtil qiniuUtil;
    @Override
    public String upload(MultipartFile file, String module) {
        try {
            byte[] b = file.getBytes();
            //获取原文件的名称
            String oldName = file.getOriginalFilename();
            String newName = StringUtil.getNewName(oldName);
            String url = qiniuUtil.uploadToQiniu(b, newName, module);
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeFile(String url) {

    }
}
