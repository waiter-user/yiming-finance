package com.java.ymjr.core.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.pojo.UserInfo;
import com.java.ymjr.core.service.UserInfoService;
import com.java.ymjr.core.vo.UserInfoQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController
@Api(tags = "会员管理")
@RequestMapping("/admin/core/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("获取会员分页列表")
    @GetMapping("/list/{current}/{size}")
    public Result listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long current,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long size,
            @ApiParam(value = "会员搜索对象", required = false)
                    UserInfoQueryVO userInfoQueryVO) {
        Page<UserInfo> pageParam = new Page<>(current, size);
        IPage<UserInfo> pageModel = userInfoService.listPage(pageParam, userInfoQueryVO);
        return Result.ok().data("pageModel", pageModel);
    }

    @ApiOperation("锁定和解锁")
    @PutMapping("/lock/{id}/{status}")
    public Result lock(
            @ApiParam(value = "用户id", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "锁定状态（0：锁定 1：正常）", required = true)
            @PathVariable("status") Integer status) {
        userInfoService.lock(id, status);
        return Result.ok().message(status == 1 ? "解锁成功" : "锁定成功");
    }
}

