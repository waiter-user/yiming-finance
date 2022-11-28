package com.java.ymjr.core.service.impl;

import com.java.mq.service.MQService;
import com.java.ymjr.core.pojo.LendItem;
import com.java.ymjr.core.mapper.LendItemMapper;
import com.java.ymjr.core.service.LendItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {
    @Resource
    private MQService mqService;
}
