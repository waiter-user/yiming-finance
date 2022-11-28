package com.java.ymjr.core.mapper;

import com.java.ymjr.core.pojo.Lend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 标的准备表 Mapper 接口
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface LendMapper extends BaseMapper<Lend> {
    //获取标的
    Lend selectLend(Long id);
}
