package com.java.ymjr.core.controller.api;


import com.java.ymjr.base.utils.JwtUtil;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.pojo.BorrowInfo;
import com.java.ymjr.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController(value = "apiBorrowInfoController")
@RequestMapping("/api/core/borrowInfo")
@Api(tags = "借款信息Controller")
public class BorrowInfoController {
    @Autowired
    private BorrowInfoService borrowInfoService;

    @GetMapping("/auth/getBorrowAmount")
    @ApiOperation(value = "获取借款额度")
    public Result getBorrowAmount(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        BigDecimal borrowAmount = borrowInfoService.getBorrowAmount(userId);
        return Result.ok().data("borrowAmount",borrowAmount);
    }

    @ApiOperation("保存借款申请")
    @PostMapping("/auth/save")
    public Result save(@RequestBody BorrowInfo borrowInfo, HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        borrowInfoService.saveBorrowInfo(borrowInfo, userId);
        return Result.ok().message("借款申请提交成功");
    }

    @ApiOperation("获取借款申请的审批状态")
    @GetMapping("/auth/getBorrowInfoStatus")
    public Result getBorrowInfoStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        Integer status = borrowInfoService.getStatusByUserId(userId);
        return Result.ok().data("borrowInfoStatus", status);
    }
}

