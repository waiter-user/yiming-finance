package com.java.ymjr.core.service;

import com.java.ymjr.core.pojo.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.ymjr.core.vo.DictVo;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface DictService extends IService<Dict> {
    void importData(InputStream stream);

    //根据上级id获取子节点数据列表
    List<Dict> getListByParentId(Long parentId);
    //获取要导出的数据
    List<DictVo>  getExportData();
    //获取父节点下的子节点
    List<Dict> findByDictCode(String dictCode);

    String getNameByParentDictCodeAndValue(String dictCode, Integer education);
}
