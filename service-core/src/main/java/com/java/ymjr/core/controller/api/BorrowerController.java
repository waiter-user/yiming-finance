package com.java.ymjr.core.controller.api;


import com.java.ymjr.base.utils.JwtUtil;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.service.BorrowerService;
import com.java.ymjr.core.vo.BorrowerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController(value = "apiBorrowerController")
@RequestMapping("/api/core/borrower")
@Api(tags = "借款人Controller")
public class BorrowerController {
    @Resource
    private BorrowerService borrowerService;

    @ApiOperation("保存借款人信息")
    @PostMapping("/auth/save")
    public Result save(@RequestBody BorrowerVO borrowerVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        borrowerService.saveBorrowerVoByUserId(borrowerVO, userId);
        return Result.ok().message("信息提交成功");
    }

    @ApiOperation("获取借款人认证状态")
    @GetMapping("/auth/getBorrowerStatus")
    public Result getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        Integer status = borrowerService.getStatusByUserId(userId);
        return Result.ok().data("borrowerStatus", status);
    }
}

