package com.java.ymjr.core.service;

import com.java.ymjr.core.pojo.TransFlow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.ymjr.core.vo.TransFlowVO;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface TransFlowService extends IService<TransFlow> {

    boolean existTransFlow(String transNo);
    void add(TransFlowVO transFlowVO);
}
