package com.java.ymjr.core.mapper;

import com.java.ymjr.core.pojo.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {
    void updateAccount(UserAccount userAccount);
}
