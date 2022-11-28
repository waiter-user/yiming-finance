package com.java.ymjr.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.ymjr.core.pojo.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.ymjr.core.vo.UserInfoQueryVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户基本信息 Mapper 接口
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    IPage<UserInfo> queryByPage(Page<UserInfo> pageParam,@Param("queryVo") UserInfoQueryVO userInfoQueryVO);
}
