package com.java.ymjr.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.java.ymjr.base.utils.JwtUtil;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.hfb.RequestHelper;
import com.java.ymjr.core.service.UserBindService;
import com.java.ymjr.core.vo.UserBindVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController(value = "apiUserBindController")
@Slf4j
@Api(tags = "会员账号绑定")
@RequestMapping("/api/core/userBind")
public class UserBindController {
    @Autowired
    private UserBindService userBindService;
    @ApiOperation("账户绑定提交数据")
    @PostMapping("/auth/bind")
    public Result bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        //返回一个表单字符串给前端
        String formStr = userBindService.commitBindUser(userBindVO, userId);

        return Result.ok().data("formStr", formStr);
    }
    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户账号绑定异步回调：" + JSON.toJSONString(paramMap));

        //校验签名
        if(!RequestHelper.isSignEquals(paramMap)) {
            log.error("用户账号绑定异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        //调用业务方法修改绑定状态
        userBindService.notify(paramMap);
        return "success";
    }
}

