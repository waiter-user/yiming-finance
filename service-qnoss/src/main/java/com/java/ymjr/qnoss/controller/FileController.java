package com.java.ymjr.qnoss.controller;

import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.qnoss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/qnoss")
@Api(tags = "文件管理api")
public class FileController {
    @Autowired
    private FileService fileService;
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public Result upload(@ApiParam(value = "上传的文件",required = true) MultipartFile file,@RequestParam(value = "module") String module) {
        String url = fileService.upload(file, module);
        return Result.ok().data("url",url);
    }

    @ApiOperation("删除oss服务器的文件")
    @DeleteMapping("/remove")
    public Result remove(
            @ApiParam(value = "要删除的文件", required = true)
            @RequestParam("url") String url) {
        fileService.removeFile(url);
        return Result.ok().message("删除成功");
    }
}
