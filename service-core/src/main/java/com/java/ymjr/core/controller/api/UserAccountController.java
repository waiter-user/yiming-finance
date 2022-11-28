package com.java.ymjr.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.ymjr.base.utils.JwtUtil;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.core.enums.TransTypeEnum;
import com.java.ymjr.core.hfb.RequestHelper;
import com.java.ymjr.core.mapper.UserInfoMapper;
import com.java.ymjr.core.pojo.UserAccount;
import com.java.ymjr.core.pojo.UserInfo;
import com.java.ymjr.core.service.TransFlowService;
import com.java.ymjr.core.service.UserAccountService;
import com.java.ymjr.core.vo.TransFlowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@RestController(value = "apiUserAccountController")
@RequestMapping("/api/core/userAccount")
@Api(tags = "会员账户Controller")
@Slf4j
public class UserAccountController {
    @Autowired
    private TransFlowService transFlowService;
    @Autowired
    private UserAccountService userAccountService;
    @Resource
    private UserInfoMapper userInfoMapper;

    @ApiOperation("充值")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public Result commitCharge(
            @ApiParam(value = "充值金额", required = true)
            @PathVariable BigDecimal chargeAmt, HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return Result.ok().data("formStr", formStr);
    }

    @ApiOperation("用户充值异步回调")
    @PostMapping("/notify")
    public String notify(Map<String, Object> paramMap) {
        String transNo = String.valueOf(paramMap.get("agentBillNo"));
        boolean b = transFlowService.existTransFlow(transNo);
        if (b) {
            log.warn("幂等性返回");
            return "success";
        }
        //获取参数中的bindCode,chargeAmt的值
        String bindCode = String.valueOf(paramMap.get("bindCode"));
        String chargeAmt = (String) paramMap.get("chargeAmt"); //充值金额
        //根据bind_code去查user_info表中的id(用户id)，name
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name").eq("bind_code", bindCode);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        //更新user_account表中的可用金额(根据user_id)
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        //充值金额
        BigDecimal chargeAmont = new BigDecimal(chargeAmt);
        userAccount.setAmount(chargeAmont);
        baseMapper.updateAccount(userAccount);

        //创建BO对象
        TransFlowVO transFlowVO = new TransFlowVO(userInfo, transNo, TransTypeEnum.RECHARGE, chargeAmont, "投资人充值");
        transFlowService.add(transFlowVO);
        //网络阻塞(有异常，阻塞时间超过了1分钟)
        return "success";
    }
}
