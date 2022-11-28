package com.java.ymjr.sms.controller;

import com.java.ymjr.base.redis.RedisOptBean;
import com.java.ymjr.base.redis.utils.RandomUtil;
import com.java.ymjr.base.redis.utils.RegexValidateUtil;
import com.java.ymjr.common.pojo.ResponseEnum;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理api")
public class SmsController {
    @Autowired
    private RedisOptBean redisOptBean;
    @Autowired
    private SmsService smsService;

    @GetMapping("/send/{phone}")
    @ApiOperation(value = "发送手机验证码")
    public Result sendMessage(@ApiParam(value = "手机号",required = true) @PathVariable String phone) {
        //判断手机号是否为空
        if(StringUtils.isEmpty(phone)){
            return Result.setResult(ResponseEnum.MOBILE_NULL_ERROR);
        }
        if(!RegexValidateUtil.checkphone(phone)){
            return Result.setResult(ResponseEnum.MOBILE_ERROR);
        }
        //生成验证码
        String code = RandomUtil.generateValidateCode(6);
        //发送短信
        boolean b = smsService.sendShortMessage(phone, code);
        if(b){
            //缓存到redis中5分钟
            String key="ymjr:sms:code:"+phone;
            redisOptBean.set(key,code,60*5);
            return Result.ok().message("短信发送成功!");
        }
        return Result.error().message("短信发送失败!");
    }
}
