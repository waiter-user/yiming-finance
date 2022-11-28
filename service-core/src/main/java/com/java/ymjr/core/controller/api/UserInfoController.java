package com.java.ymjr.core.controller.api;


import com.alibaba.nacos.common.utils.StringUtils;
import com.java.ymjr.base.redis.RedisOptBean;
import com.java.ymjr.base.redis.utils.JwtUtil;
import com.java.ymjr.base.redis.utils.RegexValidateUtil;
import com.java.ymjr.common.pojo.Assert;
import com.java.ymjr.common.pojo.ResponseEnum;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.common.pojo.YmjrException;
import com.java.ymjr.core.service.UserInfoService;
import com.java.ymjr.core.vo.LoginVO;
import com.java.ymjr.core.vo.RegisterVO;
import com.java.ymjr.core.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController(value = "apiUserInfoController")
@Api(tags = "会员接口")
@RequestMapping("/api/core/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisOptBean redisOptBean;

    @PostMapping("/register")
    @ApiOperation(value = "会员注册")
    public Result register(@RequestBody RegisterVO registerVO) {
        //1.获取手机号，密码，验证码
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();
        //2.判断手机号是否为空
//        if (StringUtils.isEmpty(mobile)) {
//            return Result.setResult(ResponseEnum.MOBILE_NULL_ERROR);
//        }
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        //3.判断手机号格式是否正确
//        if (!RegexValidateUtil.checkphone(mobile)) {
//            return Result.setResult(ResponseEnum.MOBILE_ERROR);
//        }
        Assert.isTrue(RegexValidateUtil.checkphone(mobile),ResponseEnum.MOBILE_ERROR);
        //4.判断密码是否为空
//        if (StringUtils.isEmpty(password)) {
//            return Result.setResult(ResponseEnum.PASSWORD_NULL_ERROR);
//        }
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        //5.判断验证码是否为空
//        if (StringUtils.isEmpty(code)) {
//            return Result.setResult(ResponseEnum.CODE_NULL_ERROR);
//        }
        Assert.notEmpty(code,ResponseEnum.CODE_NULL_ERROR);
        //6.判断验证码是否正确
        String key = "ymjr:sms:code:" + mobile;
        //6.1获取redis中储存的验证码
        String codeInRedis = redisOptBean.get(key).toString();
        if (null == codeInRedis || !codeInRedis.equals(code)) {
            return Result.setResult(ResponseEnum.CODE_ERROR);
        }
        //7.调用service中的业务方法
        userInfoService.register(registerVO);
        return Result.ok().message("注册成功!");
    }

    @ApiOperation("验证手机号是否已注册")
    @GetMapping("/checkPhone/{phone}")
    public Result checkPhone(@PathVariable String phone) {
        boolean f = userInfoService.existsPhone(phone);
        if (f) {
            throw new YmjrException(ResponseEnum.MOBILE_EXIST_ERROR);
        }
        return Result.ok().message("用户未注册");
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean result = JwtUtil.checkToken(token);
        if (result) {
            return Result.ok();
        } else {
            return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO, HttpServletRequest request) {

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        //判断信息是否为空
        //判断手机号是否为空
        if (StringUtils.isEmpty(mobile)) {
            return Result.setResult(ResponseEnum.MOBILE_NULL_ERROR);
        }
        //校验手机号格式
        if (!RegexValidateUtil.checkphone(mobile)) {
            return Result.setResult(ResponseEnum.MOBILE_ERROR);
        }

        //判断密码是否为空
        if (StringUtils.isEmpty(password)) {
            return Result.setResult(ResponseEnum.PASSWORD_NULL_ERROR);
        }

        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO, ip);
        return Result.ok().data("userInfo", userInfoVO);
    }
}

