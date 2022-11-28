package com.java.ymjr.core.service;

import com.java.ymjr.core.pojo.BorrowInfo;
import com.java.ymjr.core.pojo.Lend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.ymjr.core.vo.BorrowInfoApprovalVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface LendService extends IService<Lend> {
    //创建标的
    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo);
    //获取标的列表
    List<Lend> getList();
    //获取标的详情
    Map<String, Object> getLendDetail(Long id);
}
