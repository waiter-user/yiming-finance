package com.java.ymjr.core.controller;


import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.pojo.Lend;
import com.java.ymjr.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController
@RequestMapping("/admin/core/lend")
@Api(tags = "标的管理")
public class LendController {

    @Resource
    private LendService lendService;

    @ApiOperation("标的列表")
    @GetMapping("/list")
    public Result list() {
        List<Lend> lendList = lendService.getList();
        return Result.ok().data("list", lendList);
    }

    @ApiOperation("获取标的详情")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long id) {
        Map<String, Object> result = lendService.getLendDetail(id);
        return Result.ok().data("lendDetail", result);
    }
}

