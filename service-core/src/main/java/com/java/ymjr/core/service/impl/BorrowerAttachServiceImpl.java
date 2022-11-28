package com.java.ymjr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.ymjr.core.pojo.BorrowerAttach;
import com.java.ymjr.core.mapper.BorrowerAttachMapper;
import com.java.ymjr.core.service.BorrowerAttachService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
public class BorrowerAttachServiceImpl extends ServiceImpl<BorrowerAttachMapper, BorrowerAttach> implements BorrowerAttachService {
    @Override
    public List<BorrowerAttach> getBorrowerAttachList(Long id) {
        QueryWrapper<BorrowerAttach> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("image_type","image_url")
                .eq("borrower_id",id);
        List<BorrowerAttach> borrowerAttaches = baseMapper.selectList(queryWrapper);
        return borrowerAttaches;
    }
}
