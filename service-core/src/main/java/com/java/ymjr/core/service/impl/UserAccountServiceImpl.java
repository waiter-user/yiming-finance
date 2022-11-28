package com.java.ymjr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.ymjr.core.enums.TransTypeEnum;
import com.java.ymjr.core.mapper.TransFlowMapper;
import com.java.ymjr.core.mapper.UserInfoMapper;
import com.java.ymjr.core.pojo.TransFlow;
import com.java.ymjr.core.pojo.UserAccount;
import com.java.ymjr.core.mapper.UserAccountMapper;
import com.java.ymjr.core.pojo.UserInfo;
import com.java.ymjr.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private TransFlowMapper transFlowMapper;
    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        return null;
    }

    @Override
    public String notify(Map<String, Object> paramMap) {
        //处理账户数据
        String bindCode = (String) paramMap.get("bindCode"); //充值人绑定协议号
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
        //新增交易流水
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        String transNo = String.valueOf(paramMap.get("agentBillNo"));

        transFlow.setTransNo(transNo);
        transFlow.setTransType(TransTypeEnum.RECHARGE.getTransType());
        transFlow.setTransTypeName(TransTypeEnum.RECHARGE.getTransTypeName());
        transFlow.setTransAmount(chargeAmont);
        transFlow.setMemo("投资人充值");
        transFlowMapper.insert(transFlow);
        return "success";
    }
}
