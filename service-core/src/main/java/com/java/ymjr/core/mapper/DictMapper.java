package com.java.ymjr.core.mapper;

import com.java.ymjr.core.pojo.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.ymjr.core.vo.DictVo;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface DictMapper extends BaseMapper<Dict> {
    void insertBatch(List<DictVo> list);
}
