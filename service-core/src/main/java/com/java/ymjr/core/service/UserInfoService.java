package com.java.ymjr.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.ymjr.core.pojo.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.ymjr.core.vo.LoginVO;
import com.java.ymjr.core.vo.RegisterVO;
import com.java.ymjr.core.vo.UserInfoQueryVO;
import com.java.ymjr.core.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVO registerVO);

    boolean existsPhone(String phone);

    UserInfoVO login(LoginVO loginVO, String ip);

    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQueryVO userInfoQueryVO);

    void lock(Long id, Integer status);
}
