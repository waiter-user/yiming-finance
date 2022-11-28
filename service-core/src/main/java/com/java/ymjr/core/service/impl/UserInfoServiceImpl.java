package com.java.ymjr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.ymjr.base.redis.utils.JwtUtil;
import com.java.ymjr.base.redis.utils.MD5;
import com.java.ymjr.common.pojo.ResponseEnum;
import com.java.ymjr.common.pojo.Result;
import com.java.ymjr.common.pojo.YmjrException;
import com.java.ymjr.core.mapper.UserAccountMapper;
import com.java.ymjr.core.mapper.UserLoginRecordMapper;
import com.java.ymjr.core.pojo.UserAccount;
import com.java.ymjr.core.pojo.UserInfo;
import com.java.ymjr.core.mapper.UserInfoMapper;
import com.java.ymjr.core.pojo.UserLoginRecord;
import com.java.ymjr.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.ymjr.core.vo.LoginVO;
import com.java.ymjr.core.vo.RegisterVO;
import com.java.ymjr.core.vo.UserInfoQueryVO;
import com.java.ymjr.core.vo.UserInfoVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void register(RegisterVO registerVO) {
        //1.判断手机号是否被注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", registerVO.getMobile());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            //手机号已注册
            throw new YmjrException(ResponseEnum.MOBILE_EXIST_ERROR);
        }
        //插入用户基本信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVO.getUserType());
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setStatus(UserInfo.STATUS_NORMAL); //正常
        //设置一张默认的头像图片
        userInfo.setHeadImg(UserInfo.DEFAULT_AVATAR);
        baseMapper.insert(userInfo);

        //插入用户账户信息
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);
    }

    @Override
    public boolean existsPhone(String phone) {
        //判断手机号是否已被注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", phone);
        // 构造一个select count(*)  from user_info where mobile='xxx'
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return true;
            //手机号已存在
            // throw new YmjrException(ResponseEnum.MOBILE_EXIST_ERROR);
        }
        return false;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public UserInfoVO login(LoginVO loginVO, String ip) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Integer userType = loginVO.getUserType();

        //用户是否存在
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        queryWrapper.eq("user_type", userType);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (null == userInfo) {
            //手机号错误导致用户查不到 ,LOGIN_MOBILE_ERROR(208, "用户不存在"),
            throw new YmjrException(ResponseEnum.LOGIN_MOBILE_ERROR);
        }
        //比较密码
        boolean f = userInfo.getPassword().equals(MD5.encrypt(password));
        if (!f) {
            throw new YmjrException(ResponseEnum.LOGIN_PASSWORD_ERROR);
        }
        //锁定状态，用户是否被禁用
        if (userInfo.getStatus().equals(UserInfo.STATUS_LOCKED)) {
            throw new YmjrException(ResponseEnum.LOGIN_LOKED_ERROR);
        }
        //登录成功，记录登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);
        //生成Token
        String token = JwtUtil.createToken(userInfo.getId(), userInfo.getName());
        //组装一个UserInfoVO对象
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setToken(token);
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setMobile(userInfo.getMobile());
        userInfoVO.setUserType(userType);
        //返回对象
        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQueryVO userInfoQueryVO) {
        //调用Mapper中的方法根据条件查询
        return baseMapper.queryByPage(pageParam, userInfoQueryVO);
    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);

    }
}
