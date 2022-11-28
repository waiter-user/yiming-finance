package com.java.ymjr.core.controller.api;


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
@RestController(value = "apiDictController")
@RequestMapping("/api/core/dict")
@Api(tags = "会员平台数据字典的接口")
public class DictController {
    @Autowired
    private DictService dictService;

    @ApiOperation("根据dictCode获取子节点列表")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(
            @ApiParam(value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok().data("dictList", list);
    }
}

