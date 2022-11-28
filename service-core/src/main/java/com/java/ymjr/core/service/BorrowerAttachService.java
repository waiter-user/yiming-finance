package com.java.ymjr.core.service;

import com.java.ymjr.core.pojo.BorrowerAttach;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {
    //根据借款人编号获取它对应的附件集合
    List<BorrowerAttach> getBorrowerAttachList(Long id);
}
