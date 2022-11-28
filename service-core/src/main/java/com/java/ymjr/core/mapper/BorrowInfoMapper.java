package com.java.ymjr.core.mapper;

import com.java.ymjr.core.pojo.BorrowInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.ymjr.core.vo.BorrowInfoVO;

import java.util.List;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {
    //获取借款信息列表
    List<BorrowInfoVO> selectBorrowInfoList();
}
