package com.java.ymjr.core.controller;


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
@RestController
@RequestMapping("/admin/core/integralGrade")
@Api(tags = "用户积分等级前端接口")
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

    @ApiOperation(value = "新增积分等级列表")
    @PostMapping("/save")
    public Result save(@RequestBody IntegralGrade integralGrade) {
        boolean b = gradeService.save(integralGrade);
        if (b) {
            return Result.ok().message("新增积分等级成功!");
        }else{
            return Result.error().message("新增积分等级失败!");
        }
    }

    @ApiOperation(value = "获取积分等级")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable("id")long id) {
        IntegralGrade grade = gradeService.getById(id);
        Result result = Result.ok().data("integralGrade", grade);
        return result;
    }

    @ApiOperation(value = "修改积分等级")
    @PutMapping("/updateById")
    public Result updateById(@RequestBody IntegralGrade integralGrade) {
        boolean b = gradeService.updateById(integralGrade);
        if (b) {
            return Result.ok().message("修改积分等级成功!");
        }else{
            return Result.error().message("修改积分等级失败!");
        }
    }
}

