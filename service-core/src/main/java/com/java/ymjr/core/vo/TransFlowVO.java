package com.java.ymjr.core.vo;

import com.java.ymjr.core.enums.TransTypeEnum;
import com.java.ymjr.core.pojo.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransFlowVO {
    //交易流水对应的用户信息
    private UserInfo userInfo;
    private String transNo;
    //交易类型编号和名称
    private TransTypeEnum transTypeEnum;
    //交易金额
    private BigDecimal chargeAmont;
    private String memo;
}
