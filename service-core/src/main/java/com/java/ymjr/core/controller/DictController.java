package com.java.ymjr.core.controller;


import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.pojo.Dict;
import com.java.ymjr.core.service.DictService;
import com.java.ymjr.core.utils.EasyExcelUtil;
import com.java.ymjr.core.vo.DictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController
@RequestMapping("/admin/core/dict")
@Api(tags = "管理平台数据字典的接口")
public class DictController {
    @Autowired
    private DictService dictService;

    @PostMapping("/import")
    @ApiOperation("Excel批量导入数据字典")
    public Result batchImport(
            @ApiParam(value = "Excel文件", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            //调用Service的方法
            dictService.importData(file.getInputStream());
            return Result.ok().message("上传Excel成功");
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败");
        }
    }

    @GetMapping("/getListByParentId/{parentId}")
    @ApiOperation("根据父id获取子节点列表")
    public Result getListByParentId(
            @ApiParam(value = "父节点id", required = true)
            @PathVariable Long parentId) {
        List<Dict> list = dictService.getListByParentId(parentId);
        Result result = Result.ok().data("list", list);
        return result;
    }

    @GetMapping("/export")
    @ApiOperation("导出数据到Excel")
    public void exportData(
            @ApiParam(value = "响应对象", required = true)
                    HttpServletResponse response) {
        List<DictVo> list = dictService.getExportData();
        EasyExcelUtil.exportData(response, DictVo.class, "数据字典表", list);
    }
}

