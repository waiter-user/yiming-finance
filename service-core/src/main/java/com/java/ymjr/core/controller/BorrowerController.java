package com.java.ymjr.core.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.pojo.Borrower;
import com.java.ymjr.core.service.BorrowerService;
import com.java.ymjr.core.vo.BorrowerApprovalVO;
import com.java.ymjr.core.vo.BorrowerDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Api(tags = "借款人管理Controller")
@RestController("adminBorrowerController")
@RequestMapping("/admin/core/borrower")
@Slf4j
public class BorrowerController {
    @Autowired
    private BorrowerService borrowerService;

    @ApiOperation("获取借款人分页列表")
    @GetMapping("/list/{current}/{size}")
    public Result listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long current,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long size,
            @ApiParam(value = "查询关键字", required = false)
            @RequestParam(value = "keyword", required = false) String keyword) {
        //这里@RequestParam不能省略的，否则keyword被解析为请求体参数，GET请求不能有body
        Page<Borrower> pageParam = new Page<Borrower>(current, size);
        IPage<Borrower> pageModel = borrowerService.listPage(pageParam, keyword);
        return Result.ok().data("pageModel", pageModel);
    }

    @ApiOperation("获取借款人详情")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "借款人编号", required = true)
            @PathVariable Long id) {
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(id);
        return Result.ok().data("borrowerDetailVO", borrowerDetailVO);
    }

    @ApiOperation("借款额度审核")
    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO) {
        borrowerService.approval(borrowerApprovalVO);
        return Result.ok().message("审核完成");
    }
}

