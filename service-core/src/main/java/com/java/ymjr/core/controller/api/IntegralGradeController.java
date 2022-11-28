package com.java.ymjr.core.controller.api;


import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.pojo.IntegralGrade;
import com.java.ymjr.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController(value = "apiIntegralGradeController")
@RequestMapping("/api/core/integralGrade")
@Api(tags = "会员积分等级前端接口")
public class IntegralGradeController {
    @Autowired
    private IntegralGradeService gradeService;

    @ApiOperation(value = "获取积分等级列表")
    @GetMapping("/findAll")
    public Result findAll() {
        List<IntegralGrade> list = gradeService.list();
        Result result = Result.ok().data("list", list);
        return result;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除积分等级")
    public Result removeById(@ApiParam(name="id", value = "积分等级id") @PathVariable("id") Long id) {
        boolean b = gradeService.removeById(id);
        Result result = Result.ok().data("flag", b);
        return result;
    }
}

