package com.java.ymjr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.ymjr.core.enums.TransTypeEnum;
import com.java.ymjr.core.pojo.TransFlow;
import com.java.ymjr.core.mapper.TransFlowMapper;
import com.java.ymjr.core.pojo.UserInfo;
import com.java.ymjr.core.service.TransFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.ymjr.core.vo.TransFlowVO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {
    @Override
    public boolean existTransFlow(String transNo) {
        QueryWrapper<TransFlow> wrapper=new QueryWrapper<>();
        wrapper.eq("trans_no",transNo);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0;
    }

    @Override
    public void add(TransFlowVO transFlowVO) {
        //新增交易流水
        TransFlow transFlow = new TransFlow();
        UserInfo userInfo = transFlowVO.getUserInfo();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());

        transFlow.setTransNo(transFlowVO.getTransNo());
        TransTypeEnum transTypeEnum = transFlowVO.getTransTypeEnum();
        transFlow.setTransType(transTypeEnum.getTransType());
        transFlow.setTransTypeName(transTypeEnum.getTransTypeName());
        transFlow.setTransAmount(transFlowVO.getChargeAmont());
        transFlow.setMemo(transFlowVO.getMemo());
        baseMapper.insert(transFlow);
    }
}
