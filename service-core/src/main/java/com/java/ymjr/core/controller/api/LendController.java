package com.java.ymjr.core.controller.api;


import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.pojo.Lend;
import com.java.ymjr.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController(value = "apiLendController")
@RequestMapping("/api/core/lend")
@Api(tags = "标的Controller")
public class LendController {

    @Autowired
    private LendService lendService;

    @ApiOperation("获取标的列表")
    @GetMapping("/list")
    public Result list() {
        List<Lend> lendList = lendService.getList();
        return Result.ok().data("lendList", lendList);
    }
}

