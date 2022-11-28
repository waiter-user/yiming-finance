package com.java.ymjr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.ymjr.base.redis.RedisOptBean;
import com.java.ymjr.core.pojo.Dict;
import com.java.ymjr.core.mapper.DictMapper;
import com.java.ymjr.core.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.ymjr.core.utils.EasyExcelUtil;
import com.java.ymjr.core.vo.DictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private RedisOptBean redisOptBean;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(InputStream stream) {
        List<DictVo> dictVos = EasyExcelUtil.readExcel(stream, DictVo.class, 0);
        baseMapper.insertBatch(dictVos);
    }

    @Override
    public List<Dict> getListByParentId(Long parentId) {
        //获取Redis中的数据，key为 ymjr:core:parentId值，值就是对应的数据集合
        String key="ymjr:core:"+parentId;
        List list = redisOptBean.lrange(key, 0, -1);
        List<Dict> dicts =null;
        if(null==list||list.size()==0){
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.select("id", "name", "dict_code", "value").eq("parent_id", parentId);
            dicts = baseMapper.selectList(wrapper);
            dicts.forEach(e -> {
                //判断当前节点是否有子节点
                boolean b = hasChildren(e.getId());
                e.setHasChildren(b);
                //存储到redis中,有效时间2天
                redisOptBean.rpush(key,e,2*24*60*60);
            });
        }
        return dicts;
    }

    //判断id对应的节点是否有子节点
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer i = baseMapper.selectCount(wrapper);//会自动生成select count(*) from 表
        return i > 0;
    }

    @Override
    public List<DictVo> getExportData() {
        List<Dict> dicts = baseMapper.selectList(null);

        List<DictVo> list = new ArrayList<>();
        dicts.forEach(e -> {
            DictVo vo = new DictVo();
            vo.setId(e.getId());
            vo.setName(e.getName());
            vo.setDictCode(e.getDictCode());
            vo.setValue(e.getValue());
            vo.setParentId(e.getParentId());
            list.add(vo);
        });
        return list;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.select("id").eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        List<Dict> dicts = this.getListByParentId(dict.getId());
        return dicts;
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer education) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").eq("dict_code", dictCode);
        Dict parentDict = baseMapper.selectOne(queryWrapper);
        //Long id = parentDict.getId();

        queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name")
                .eq("parent_id", parentDict.getId())
                .eq("value",education);
        Dict dict = baseMapper.selectOne(queryWrapper);
        return dict.getName();
    }
}
