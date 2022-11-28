package com.java.ymjr.core.service;

import com.java.ymjr.core.pojo.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.ymjr.core.vo.BorrowInfoApprovalVO;
import com.java.ymjr.core.vo.BorrowInfoVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface BorrowInfoService extends IService<BorrowInfo> {
    //获取借款额度
    BigDecimal getBorrowAmount(Long userId);
    //保存借款申请信息
    void saveBorrowInfo(BorrowInfo borrowInfo, Long userId);
    //通过用户id获取状态
    Integer getStatusByUserId(Long userId);
    //获取借款信息列表
    List<BorrowInfoVO> getList();
    //获取贷款详情
    Map<String, Object> getBorrowInfoDetail(Long id);

    void approval(BorrowInfoApprovalVO borrowInfoApprovalVO);
}
