package com.java.ymjr.core.controller;


import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.service.BorrowInfoService;
import com.java.ymjr.core.vo.BorrowInfoApprovalVO;
import com.java.ymjr.core.vo.BorrowInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */

@RestController
@RequestMapping("/admin/core/borrowInfo")
@Api(tags = "借款管理Controller")
public class BorrowInfoController {
    @Autowired
    private BorrowInfoService borrowInfoService;

    @ApiOperation("借款信息列表")
    @GetMapping("/list")
    public Result list() {
        List<BorrowInfoVO> borrowInfoList = borrowInfoService.getList();
        return Result.ok().data("list", borrowInfoList);
    }

    @ApiOperation("获取借款详情")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "借款信息id", required = true)
            @PathVariable Long id) {
        Map<String, Object> borrowInfoDetail = borrowInfoService.getBorrowInfoDetail(id);
        return Result.ok().data("borrowInfoDetail", borrowInfoDetail);
    }

    @ApiOperation("审批借款信息")
    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO) {
        borrowInfoService.approval(borrowInfoApprovalVO);
        return Result.ok().message("审批完成");
    }
}

