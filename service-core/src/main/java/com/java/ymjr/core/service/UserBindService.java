package com.java.ymjr.core.service;

import com.java.ymjr.core.pojo.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.ymjr.core.vo.UserBindVO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface UserBindService extends IService<UserBind> {
    //账户绑定提交到托管平台的数据
    String commitBindUser(UserBindVO userBindVO, Long userId);
    //通知商户同步商户信息
    void notify(Map<String, Object> paramMap);
}
